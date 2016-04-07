package hu.frankdavid.hotel.entity;

import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlTransient;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class User extends AbstractEntity {
    @Email
    private String email;

    @XmlTransient
    private String passwordHash;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public boolean hasRole(Role role) {
        return getRoles().contains(role);
    }

    public abstract String getType();

    public abstract RoleSet getRoles();
}
