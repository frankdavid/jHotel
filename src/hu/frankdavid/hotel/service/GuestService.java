package hu.frankdavid.hotel.service;

import hu.frankdavid.hotel.entity.Guest;
import hu.frankdavid.hotel.entity.Reservation;
import hu.frankdavid.hotel.entity.Role;
import hu.frankdavid.hotel.entity.querydsl.QGuest;
import hu.frankdavid.hotel.entity.querydsl.QReservation;
import hu.frankdavid.hotel.form.GuestSignupForm;
import hu.frankdavid.hotel.security.RolesAllowed;
import hu.frankdavid.hotel.util.ValidationUtil;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.ws.rs.*;
import java.util.List;
import java.util.Set;

@Stateless
@Path("/guests")
public class GuestService extends BaseService {
    @Inject
    UserService userService;

    private static final ConstraintViolation<GuestSignupForm> EMAIL_VIOLATION = ConstraintViolationImpl.forBeanValidation(
            "Már létezik regisztráció ezzel az email címmel",
            "Már létezik regisztráció ezzel az email címmel",
            GuestSignupForm.class, null, null, null, PathImpl.createPathFromString("email"), null, null);

    @POST
    @Produces("application/json;charset=utf8")
    public java.util.Map<String, String> register(GuestSignupForm form,
                                                  @QueryParam("validateOnly") @DefaultValue("false") boolean validateOnly) {
        Set<ConstraintViolation<GuestSignupForm>> violations = validate(form);
        if(userService.findUserByEmail(form.getEmail()) != null) {
            violations.add(EMAIL_VIOLATION);
        }
        if (violations.isEmpty() && !validateOnly) {
            Guest guest = createGuest(form);
            persist(guest);
            loggedInUser.set(guest);
        }
        return ValidationUtil.constraintViolationSetToMap(violations);
    }

    private Guest createGuest(GuestSignupForm form) {
        Guest guest = new Guest();
        guest.setName(form.getName());
        guest.setPhone(form.getPhone());
        guest.setEmail(form.getEmail());
        guest.setPasswordHash(userService.encodePassword(form.getPassword(), guest));
        return guest;
    }

    public Reservation findCurrentReservation(Guest guest) {
        return query(QReservation.reservation)
                .where(QReservation.reservation.guest.eq(guest).and(QReservation.reservation.checkedIn).and(QReservation.reservation.checkedOut.not()))
                .singleResult(QReservation.reservation);
    }

    public Guest findGuestByEmailAndPassword(String email, String password) {
        QGuest user = QGuest.guest;
        Guest result = query(user).where(user.email.eq(email)).singleResult(user);
        if(result == null || !userService.checkPassword(result, password))
            return null;
        else
            return result;
    }

    @GET
    @Produces("application/json;charset=utf8")
    @Path("/loggedIn/reservations")
    @RolesAllowed(Role.GUEST)
    public List<Reservation> findCurrentUsersReservations() {
        QReservation reservation = QReservation.reservation;
        return query(reservation)
                .where(reservation.guest.id.eq(loggedInUser.getId()))
                .orderBy(reservation.startDate.asc())
                .list(reservation);
    }
}
