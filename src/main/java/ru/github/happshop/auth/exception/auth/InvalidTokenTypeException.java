package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class InvalidTokenTypeException extends AuthException {
    public InvalidTokenTypeException() {
        super("Неверный тип токена", HttpStatus.UNAUTHORIZED);
    }
}
