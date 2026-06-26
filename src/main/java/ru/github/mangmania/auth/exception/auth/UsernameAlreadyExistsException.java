package ru.github.mangmania.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.mangmania.common.error.exception.AuthException;

public class UsernameAlreadyExistsException extends AuthException {
    public UsernameAlreadyExistsException() {
        super("Это имя пользователя уже занято другим пользователем", HttpStatus.BAD_REQUEST);
    }
}
