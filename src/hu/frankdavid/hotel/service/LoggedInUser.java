package hu.frankdavid.hotel.service;

import hu.frankdavid.hotel.entity.Role;
import hu.frankdavid.hotel.entity.User;

import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@SessionScoped
public class LoggedInUser implements Serializable {
    private Long loggedInUserId;

    @PersistenceContext
    private EntityManager em;

    public void set(User user) {
        loggedInUserId = user.getId();
    }

    public User get() {
        if (loggedInUserId == null)
            return null;
        return em.find(User.class, loggedInUserId);
    }

    public void logout() {
        loggedInUserId = null;
    }

    public boolean hasRole(Role role) {
        return get() != null && get().hasRole(role);
    }

    public boolean notEmpty() {
        return loggedInUserId != null;
    }

    public Long getId() {
        return loggedInUserId;
    }
}
