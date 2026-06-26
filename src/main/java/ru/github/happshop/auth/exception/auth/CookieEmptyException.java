package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class CookieEmptyException extends AuthException {
    public CookieEmptyException() {
        super("Cookie является пустым", HttpStatus.BAD_REQUEST);
    }
}
