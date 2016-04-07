package hu.frankdavid.hotel.dto;

import hu.frankdavid.hotel.entity.User;

public class AuthenticationResult {

    private AuthenticationResult(ResultType result, User user) {
        this.result = result;
        this.user = user;
    }

    private ResultType result;

    private User user;

    public ResultType getResult() {
        return result;
    }

    public User getUser() {
        return user;
    }

    public static AuthenticationResult ok(User user) {
        return new AuthenticationResult(ResultType.OK, user);
    }

    public static AuthenticationResult userNotFound() {
        return new AuthenticationResult(ResultType.USER_NOT_FOUND, null);
    }

    public static AuthenticationResult incorrectPassword() {
        return new AuthenticationResult(ResultType.INCORRECT_PASSWORD, null);
    }

    public static enum ResultType {
        OK, USER_NOT_FOUND, INCORRECT_PASSWORD
    }
}
