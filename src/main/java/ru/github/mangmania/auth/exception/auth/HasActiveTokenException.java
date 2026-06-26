package ru.github.mangmania.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.mangmania.common.error.exception.AuthException;

public class HasActiveTokenException extends AuthException {
    public HasActiveTokenException() {
        super("Операция временно недоступна", HttpStatus.BAD_REQUEST);
    }
}
