package hu.frankdavid.hotel.service;

import hu.frankdavid.hotel.dto.AuthenticationResult;
import hu.frankdavid.hotel.entity.Guest;
import hu.frankdavid.hotel.entity.User;
import hu.frankdavid.hotel.entity.querydsl.QUser;
import hu.frankdavid.hotel.form.GuestProfileForm;
import hu.frankdavid.hotel.form.LoginForm;
import hu.frankdavid.hotel.security.RolesAllowed;
import hu.frankdavid.hotel.util.ValidationUtil;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.ws.rs.*;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@Stateless
@Path("/users")
public class UserService extends BaseService {

    @POST
    @Path("/authenticate")
    @Produces("application/json;charset=utf8")
    public AuthenticationResult authenticate(LoginForm form) {
        QUser user = QUser.user;
        if (form.getEmail() == null)
            return AuthenticationResult.userNotFound();
        User result = query(user).where(user.email.eq(form.getEmail())).singleResult(user);
        if (result == null)
            return AuthenticationResult.userNotFound();
        else if (!checkPassword(result, form.getPassword()))
            return AuthenticationResult.incorrectPassword();
        else {
            loggedInUser.set(result);
            return AuthenticationResult.ok(result);
        }
    }

    public User authenticate(String email, String password) {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail(email);
        loginForm.setPassword(password);
        AuthenticationResult authenticationResult = authenticate(loginForm);
        return authenticationResult.getUser();
    }

    public boolean checkPassword(User user, String plainPassword) {
        return user.getPasswordHash().equals(encodePassword(plainPassword, user));
    }

    @GET
    @Path("/loggedIn")
    @Produces("application/json;charset=utf8")
    @RolesAllowed
    public User getLoggedInUser() {
        return loggedInUser.get();
    }

    //    @POST
    @GET
    @Path("/logout")
    @RolesAllowed
    public void logout() {
        loggedInUser.logout();
    }

    @PUT
    @Path("/loggedIn/{id}")
    @Produces("application/json;charset=utf8")
    @RolesAllowed
    public java.util.Map<String, String> updateLoggedInUser(GuestProfileForm form) {
        if (loggedInUser.get() instanceof Guest) {
            Guest loggedInGuest = (Guest) loggedInUser.get();
            Set<ConstraintViolation<GuestProfileForm>> violations = validate(form);
            if (violations.isEmpty()) {
                loggedInGuest.setName(form.getName());
                loggedInGuest.setPhone(form.getPhone());
            }
            return ValidationUtil.constraintViolationSetToMap(violations);
        }
        return null;
    }

    public String encodePassword(String password, User user) {
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));

            return new BigInteger(1, crypt.digest()).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public User findUserByEmail(String email) {
        return query(QUser.user).where(QUser.user.email.eq(email)).singleResult(QUser.user);
    }
}
