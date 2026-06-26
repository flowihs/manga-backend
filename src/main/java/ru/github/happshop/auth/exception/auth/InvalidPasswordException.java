package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class InvalidPasswordException extends AuthException {
    public InvalidPasswordException() {
        super("Неверный пароль", HttpStatus.BAD_REQUEST);
    }
}
