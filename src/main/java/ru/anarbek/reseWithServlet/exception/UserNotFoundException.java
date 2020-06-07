package ru.anarbek.reseWithServlet.exception;

public class UserNotFoundException extends Exception {

    private final int code;

    public UserNotFoundException(String message) {
        super(message);
        this.code = 100;
    }

    public int getCode() {
        return code;
    }
}
