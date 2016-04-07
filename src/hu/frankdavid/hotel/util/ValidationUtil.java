package hu.frankdavid.hotel.util;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidationUtil {
    public static String getPathString(ConstraintViolation<?> violation) {
        StringBuilder path = new StringBuilder();
        for (Path.Node node : violation.getPropertyPath()) {
            path.append(node.getName()).append('.');
        }
        path.deleteCharAt(path.length() - 1);
        return path.toString();
    }

    public static <T> Map<String, String> constraintViolationSetToMap(Set<ConstraintViolation<T>> violations) {
        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            errors.put(getPathString(violation), violation.getMessage());
        }

        return errors;
    }
}
