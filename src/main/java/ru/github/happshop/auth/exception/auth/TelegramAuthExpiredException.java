package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class TelegramAuthExpiredException extends AuthException {
    public TelegramAuthExpiredException() {
        super("Время сесси авторизации telegram истекло", HttpStatus.UNAUTHORIZED);
    }
}
