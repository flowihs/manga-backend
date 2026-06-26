package ru.github.mangmania.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.mangmania.common.error.exception.AuthException;

public class InvalidLoginException extends AuthException {
    public InvalidLoginException() {
        super("Неверный логин или пароль", HttpStatus.UNAUTHORIZED);
    }
}
