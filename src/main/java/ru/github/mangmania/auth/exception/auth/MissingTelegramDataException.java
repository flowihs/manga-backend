package ru.github.mangmania.auth.exception.auth;

import org.springframework.http.HttpStatus;
import ru.github.mangmania.common.error.exception.AuthException;

public class MissingTelegramDataException extends AuthException {
    public MissingTelegramDataException(String message) {
        super("Отсутствует поле " + message + "в данных Telegram", HttpStatus.BAD_REQUEST);
    }
}
