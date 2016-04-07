package hu.frankdavid.hotel.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@Entity
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Guest extends User {

    private static final RoleSet GUEST_ROLES = new RoleSet(Role.GUEST);

    private String name;

    private String phone;

    @OneToMany(mappedBy = "guest")
    private List<Reservation> reservations;


    @XmlTransient
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getType() {
        return "GUEST";
    }

    @Override
    @XmlTransient
    public RoleSet getRoles() {
        return GUEST_ROLES;
    }

}
