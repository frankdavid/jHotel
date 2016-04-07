package hu.frankdavid.hotel.service;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.types.expr.BooleanExpression;
import hu.frankdavid.hotel.entity.Guest;
import hu.frankdavid.hotel.entity.Reservation;
import hu.frankdavid.hotel.entity.Role;
import hu.frankdavid.hotel.entity.Room;
import hu.frankdavid.hotel.entity.querydsl.QReservation;
import hu.frankdavid.hotel.entity.querydsl.QRoom;
import hu.frankdavid.hotel.form.RoomSpecification;
import hu.frankdavid.hotel.security.RolesAllowed;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
@Path("/rooms")
public class RoomService extends BaseService {

    @EJB
    ReservationService reservationService;

    @GET
    @RolesAllowed(Role.RECEPTION)
    public List<Room> list() {
        QRoom room = QRoom.room;
        return query(room).orderBy(room.number.asc()).list(room);
    }

    @PUT
    @RolesAllowed(Role.RECEPTION)
    @Path("/{roomId}")
    public void put(Room room) {
        merge(room);
    }

    @POST
    @RolesAllowed(Role.RECEPTION)
    public Room create(Room room) {
        persist(room);
        return room;
    }

    @POST
    @Produces("application/json;charset=utf8")
    @Path("/available")
    @RolesAllowed
    public List<RoomSpecification> searchAvailableRooms(RoomSpecification form) {
        Date startDate = form.getStartDate();
        Date endDate = form.getEndDate();
        Date dayAfterStartDate = new DateTime(startDate).plusDays(1).toDate();

        if(!isValid(form))
            return Collections.emptyList();

        QReservation reservation = QReservation.reservation;
        QRoom room = QRoom.room;
        BooleanExpression roomPredicate = new JPASubQuery().from(reservation).where(reservation.room.eq(room).and(
                reservation.startDate.between(dayAfterStartDate, endDate).or(reservation.endDate.between(dayAfterStartDate, endDate))
        )).notExists().and(room.smoking.eq(form.isSmoking())).and(room.numberOfBeds.goe(form.getNumberOfBeds()));
        if (form.isSeaView())
            roomPredicate = roomPredicate.and(room.seaView.isTrue());
        if(form.getPricePerNight() != null)
            roomPredicate = roomPredicate.and(room.pricePerNight.eq(form.getPricePerNight()));
        List<Tuple> results = query(room).where(roomPredicate)
                .groupBy(room.pricePerNight, room.seaView, room.smoking, room.numberOfBeds).orderBy(room.pricePerNight.asc())
                .list(room.pricePerNight, room.seaView, room.smoking, room.number.min(), room.id.min(), room.numberOfBeds);

        long nights = new Duration(new DateTime(startDate), new DateTime(endDate)).getStandardDays();
        List<RoomSpecification> rooms = new ArrayList<>();
        for (Tuple tuple : results) {
            RoomSpecification roomSpecification = new RoomSpecification();
            roomSpecification.setPricePerNight(tuple.get(room.pricePerNight));
            roomSpecification.setSeaView(tuple.get(room.seaView));
            roomSpecification.setSmoking(tuple.get(room.smoking));
            roomSpecification.setNumberOfBeds(tuple.get(room.numberOfBeds));
            roomSpecification.setWholePrice(nights * tuple.get(room.pricePerNight));
            roomSpecification.setRoomId(tuple.get(room.id.min()));
            if(form.getWholePrice() == null || form.getWholePrice().equals(roomSpecification.getWholePrice())) {
                System.out.println("Found room: " + tuple.get(room.number.min()));
                rooms.add(roomSpecification);
            }
        }
        return rooms;
    }

    public Room findRoomForSpecification(RoomSpecification specification) {
        List<RoomSpecification> specifications = searchAvailableRooms(specification);
        if(specifications.size() == 0)
            return null;
        return find(Room.class, specifications.get(0).getRoomId());
    }

    @RolesAllowed(Role.RECEPTION)
    public Reservation getCurrentReservation(Room room) {
        return query(QReservation.reservation)
                .where(QReservation.reservation.room.eq(room).and(QReservation.reservation.checkedIn).and(QReservation.reservation.checkedOut.not()))
                .singleResult(QReservation.reservation);
    }

    @GET
    @Path("/number/{roomNumber}/guest")
    @Produces("application/json;charset=utf8")
    @RolesAllowed(Role.EMPLOYEE)
    public Guest findGuestForRoom(@PathParam("roomNumber") String roomNumber) {
        Reservation reservation = reservationService.findReservationByRoomNumber(roomNumber);
        if(reservation == null)
            return null;
        else
            return reservation.getGuest();
    }
}
