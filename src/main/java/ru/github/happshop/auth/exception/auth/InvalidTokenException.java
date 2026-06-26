package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException() {
        super("Отсутствует или некорректный токен", HttpStatus.BAD_REQUEST);
    }
}
