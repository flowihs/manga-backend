package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class PasswordResetTokenNotFound extends AuthException {
    public PasswordResetTokenNotFound() {
        super("Токен для восстановления пароля не был найден", HttpStatus.NOT_FOUND);
    }
}
