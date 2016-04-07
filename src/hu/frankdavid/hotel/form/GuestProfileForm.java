package hu.frankdavid.hotel.form;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestProfileForm {
    @NotEmpty(message = "A nevet ki kell tölteni")
    private String name;

    @Pattern(regexp = "[\\d\\-\\(\\)]*", message = "A telefonszám hibás formátumban van!")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
