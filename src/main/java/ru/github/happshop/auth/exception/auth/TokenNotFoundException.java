package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class TokenNotFoundException extends AuthException {
    public TokenNotFoundException() {
        super("Токен не был найден", HttpStatus.NOT_FOUND);
    }
}
