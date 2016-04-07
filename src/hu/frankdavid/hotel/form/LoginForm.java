package hu.frankdavid.hotel.form;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginForm {
    @Email(message = "Hibás emailt adtál meg")
    private String email;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
