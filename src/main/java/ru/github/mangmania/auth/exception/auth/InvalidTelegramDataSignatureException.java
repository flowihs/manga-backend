package ru.github.mangmania.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.mangmania.common.error.exception.AuthException;

public class InvalidTelegramDataSignatureException extends AuthException {
    public InvalidTelegramDataSignatureException() {
        super("Недействительная подпись данных Telegram", HttpStatus.BAD_REQUEST);
    }
}
