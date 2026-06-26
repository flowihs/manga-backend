package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class RefreshTokenExpiredException extends AuthException {
    public RefreshTokenExpiredException() {
        super("Refresh токен истек", HttpStatus.UNAUTHORIZED);
    }
}
