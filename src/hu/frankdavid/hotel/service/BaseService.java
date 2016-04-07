package hu.frankdavid.hotel.service;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import hu.frankdavid.hotel.entity.AbstractEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

public class BaseService {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Validator validator;

    @Inject
    protected LoggedInUser loggedInUser;

    protected JPAQuery query() {
        return new JPAQuery(em);
    }

    protected JPAQuery query(EntityPath<?> path) {
        return new JPAQuery(em).from(path);
    }

    protected void persist(AbstractEntity entity) {
        em.persist(entity);
    }

    protected <T> List<T> findAll(EntityPath<T> path) {
        return query(path).list(path);
    }

    protected void merge(Object entity) {
        em.merge(entity);
    }

    protected <T> T find(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }

    protected void remove(Object entity) {
        em.remove(entity);
    }

    protected <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validator.validate(object, groups);
    }

    protected boolean isValid(Object object, Class<?>... groups) {
        return validate(object, groups).size() == 0;
    }

    protected void removeById(Class<?> clazz, Long id) {
        em.remove(em.getReference(clazz, id));
    }
}
