package hu.frankdavid.hotel.service;

import hu.frankdavid.hotel.entity.*;
import hu.frankdavid.hotel.entity.querydsl.QPurchase;
import hu.frankdavid.hotel.entity.querydsl.QReservation;
import hu.frankdavid.hotel.form.RoomSpecification;
import hu.frankdavid.hotel.security.RolesAllowed;
import org.jboss.resteasy.spi.UnauthorizedException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import java.util.Date;
import java.util.List;

@Stateless
@Path("/reservations")
public class ReservationService extends BaseService {
    @EJB
    RoomService roomService;

    public Reservation findReservationByRoomNumber(String roomNumber) {
        QReservation reservation = QReservation.reservation;
        return query(reservation)
                .where(reservation.room.number.eq(roomNumber).and(reservation.checkedIn.isTrue()).and(reservation.checkedOut.isFalse()))
                .orderBy(reservation.startDate.desc())
                .singleResult(reservation);
    }

    @POST
    @Produces("application/json")
    @RolesAllowed(Role.GUEST)
    public boolean makeReservation(RoomSpecification specification) {
        if (!isValid(specification))
            return false;
        Room room = roomService.findRoomForSpecification(specification);
        if (room == null)
            return false;
        if (!(loggedInUser.get() instanceof Guest))
            return false;
        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setGuest((Guest) loggedInUser.get());
        reservation.setStartDate(specification.getStartDate());
        reservation.setEndDate(specification.getEndDate());
        persist(reservation);
        return true;
    }

    @DELETE
    @Path("/{reservationId}")
    @RolesAllowed(Role.GUEST)
    public void cancelReservation(@PathParam("reservationId") Long reservationId) {
        Reservation reservation = find(Reservation.class, reservationId);
        if(reservation != null && reservation.getGuest().equals(loggedInUser.get()) && reservation.isCancellable())
            remove(reservation);
    }

    @GET
    @Produces("application/json;charset=utf8")
    @RolesAllowed
    @Path("/{reservationId}/purchases")
    public List<Purchase> findPurchasesForReservationWithAuthorization(@PathParam("reservationId") Long reservationId) {
        return findPurchasesForReservationWithAuthorization(reservationId, loggedInUser.get());
    }

    public List<Purchase> findPurchasesForReservationWithAuthorization(Long reservationId, User user) {
        Reservation reservation = find(Reservation.class, reservationId);
        if(reservation == null)
            return null;
        if(user == null || !user.hasRole(Role.EMPLOYEE) && !reservation.getGuest().equals(user)) {
            throw new UnauthorizedException();
        }
        return findPurchasesForReservation(reservationId);
    }

    public List<Purchase> findPurchasesForReservation(Long reservationId) {
        QPurchase purchase = QPurchase.purchase;
        return query(purchase).where(purchase.reservation.id.eq(reservationId)).list(purchase);
    }

    @POST
    @Path("/{reservationId}/checkIn")
    @Produces("application/json;charset=utf8")
    @RolesAllowed(Role.RECEPTION)
    public boolean checkIn(@PathParam("reservationId") Long reservationId) {
        Reservation reservation = find(Reservation.class, reservationId);
        if(reservation != null && reservation.isCheckInAvailable()) {
            reservation.checkIn();
            return true;
        }
        return false;
    }

    @GET
    @Path("/{reservationId}/fullPrice")
    @Produces("application/json;charset=utf8")
    @RolesAllowed(Role.RECEPTION)
    public long getFullPrice(@PathParam("reservationId") Long reservationId) {
        Reservation reservation = find(Reservation.class, reservationId);
        if(reservation != null) {
            return reservation.getFullPrice();
        }
        return 0;
    }

    @POST
    @Path("/{reservationId}/checkOut")
    @Produces("application/json;charset=utf8")
    @RolesAllowed(Role.RECEPTION)
    public boolean checkOut(@PathParam("reservationId") Long reservationId) {
        Reservation reservation = find(Reservation.class, reservationId);
        if(reservation != null && reservation.isCheckOutAvailable()) {
            reservation.checkOut();
            return true;
        }
        return false;

    }

    @GET
    @Path("/current")
    @Produces("application/json;charset=utf8")
    @RolesAllowed(Role.EMPLOYEE)
    public List<Reservation> getCurrentReservations() {
        QReservation reservation = QReservation.reservation;
        List<Reservation> results = query(reservation)
                .where(reservation.startDate.loe(new Date()).and(reservation.endDate.goe(new Date())))
                .orderBy(reservation.guest.name.asc())
                .list(reservation);
        for (Reservation result : results) {
            result.calculateFullPrice();
        }
        return results;
    }


}
