package hu.frankdavid.hotel.security;

import hu.frankdavid.hotel.entity.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RolesAllowed {
    Role[] value() default {};
}
