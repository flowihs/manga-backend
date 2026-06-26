package ru.github.mangmania.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.mangmania.common.error.exception.AuthException;

public class InvalidTokenTypeException extends AuthException {
    public InvalidTokenTypeException() {
        super("Неверный тип токена", HttpStatus.UNAUTHORIZED);
    }
}
