package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class EmailAlreadyExistsException extends AuthException {
    public EmailAlreadyExistsException() {
        super("Эта почта уже занята другим пользователем", HttpStatus.BAD_REQUEST);
    }
}
