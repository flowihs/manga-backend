package ru.github.mangmania.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.mangmania.common.error.exception.AuthException;

public class TelegramAuthExpiredException extends AuthException {
    public TelegramAuthExpiredException() {
        super("Время сесси авторизации telegram истекло", HttpStatus.UNAUTHORIZED);
    }
}
