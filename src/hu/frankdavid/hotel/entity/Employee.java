package hu.frankdavid.hotel.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Employee extends User {

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = new RoleSet();

    @Override
    public String getType() {
        return "EMPLOYEE";
    }

    @Override
    public RoleSet getRoles() {
        return new RoleSet(roles);
    }

    public void setRoles(RoleSet roles) {
        this.roles = roles.delegate();
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
