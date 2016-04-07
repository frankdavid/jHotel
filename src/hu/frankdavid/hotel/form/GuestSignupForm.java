package hu.frankdavid.hotel.form;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestSignupForm {
    @NotEmpty(message = "Az emailt meg kell adni")
    @Email(message = "Hibás emailt adtál meg")
    String email;

    @NotEmpty(message = "A nevet ki kell tölteni")
    String name;

    @Pattern(regexp = "[\\d\\-\\(\\)]*", message = "A telefonszám hibás formátumban van!")
    String phone;

    @Size(min = 4, message = "A jelszó legalább {min} karakter hosszú legyen")
    String password;

    String password2;

    @AssertTrue(message = "A jelszavaknak egyezniük kell!")
    public boolean isPasswordsMatch() {
        return password.equals(password2);
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
