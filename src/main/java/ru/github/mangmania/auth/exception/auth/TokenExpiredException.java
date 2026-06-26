package ru.github.mangmania.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.mangmania.common.error.exception.AuthException;

public class TokenExpiredException extends AuthException {
    public TokenExpiredException() {
        super("Токен устек", HttpStatus.BAD_REQUEST);
    }
}
