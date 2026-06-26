package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class GoogleAccountConflictException extends AuthException {
    public GoogleAccountConflictException() {
        super("Email уже зарегистрирован, но с другим Google аакнаутом", HttpStatus.CONFLICT);
    }
}
