package hu.frankdavid.hotel;

import hu.frankdavid.hotel.util.ValidationUtil;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

public class ValidationError {

    private String message;

    private String path;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static <T> Set<ValidationError> fromConstraintViolationSet(Set<ConstraintViolation<T>> violations) {
        Set<ValidationError> errors = new HashSet<>();
        for (ConstraintViolation<?> violation : violations) {
            ValidationError error = new ValidationError();
            error.setMessage(violation.getMessage());
            error.setPath(ValidationUtil.getPathString(violation));
            errors.add(error);
        }
        return errors;
    }

}
