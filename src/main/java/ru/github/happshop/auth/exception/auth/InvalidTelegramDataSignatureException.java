package ru.github.happshop.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.AuthException;

public class InvalidTelegramDataSignatureException extends AuthException {
    public InvalidTelegramDataSignatureException() {
        super("Недействительная подпись данных Telegram", HttpStatus.BAD_REQUEST);
    }
}
