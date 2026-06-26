package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class TokenExpiredException extends AuthException {
    public TokenExpiredException() {
        super("Токен устек", HttpStatus.BAD_REQUEST);
    }
}
