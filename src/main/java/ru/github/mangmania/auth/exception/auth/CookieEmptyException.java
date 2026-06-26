package ru.github.mangmania.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.mangmania.common.error.exception.AuthException;

public class CookieEmptyException extends AuthException {
    public CookieEmptyException() {
        super("Cookie является пустым", HttpStatus.BAD_REQUEST);
    }
}
