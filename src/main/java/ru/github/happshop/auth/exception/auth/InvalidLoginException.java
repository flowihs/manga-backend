package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class InvalidLoginException extends AuthException {
    public InvalidLoginException() {
        super("Неверный логин или пароль", HttpStatus.UNAUTHORIZED);
    }
}
