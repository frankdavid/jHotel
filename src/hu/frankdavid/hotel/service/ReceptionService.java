package hu.frankdavid.hotel.service;

import hu.frankdavid.hotel.entity.Role;
import hu.frankdavid.hotel.security.RolesAllowed;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

@Stateless
@Path("/reception")
@RolesAllowed(Role.RECEPTION)
public class ReceptionService extends BaseService {

//    @POST
//    @Path("/checkin/{guestId}")
//    public boolean checkIn(@PathParam("guestId") Long guestId) {
//        Guest guest = find(Guest.class, guestId);
//        Reservation reservationOnToday = null;
//        for (Reservation reservation : guest.getReservations()) {
//            if (isToday(reservation.getStartDate())) {
//                reservationOnToday = reservation;
//            }
//        }
//        if (reservationOnToday != null) {
//            reservationOnToday.checkIn();
//            return true;
//        }
//        return false;
//    }



}
