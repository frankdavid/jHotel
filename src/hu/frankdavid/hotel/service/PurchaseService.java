package hu.frankdavid.hotel.service;

import hu.frankdavid.hotel.entity.Employee;
import hu.frankdavid.hotel.entity.Purchase;
import hu.frankdavid.hotel.entity.Reservation;
import hu.frankdavid.hotel.entity.Role;
import hu.frankdavid.hotel.form.PurchaseForm;
import hu.frankdavid.hotel.security.RolesAllowed;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Stateless
@Path("/purchases")
public class PurchaseService extends BaseService {

    @Inject
    ReservationService reservationService;

    @POST
    @RolesAllowed({Role.RECEPTION, Role.RESTAURANT, Role.WELLNESS})
    public void purchase(PurchaseForm form) {
        if(!(loggedInUser.get() instanceof Employee)) {
            throw new IllegalStateException("Logged in user is not an employee");
        }
        Employee loggedInEmployee = ((Employee) loggedInUser.get());
        Reservation reservation = reservationService.findReservationByRoomNumber(form.getRoomNumber());
        if(reservation != null) {
            Purchase purchase = new Purchase();
            purchase.setAmount(form.getAmount());
            purchase.setCreatedBy(loggedInEmployee);
            purchase.setDescription(form.getDescription());
            purchase.setReservation(reservation);
            persist(purchase);
        }
    }
}
