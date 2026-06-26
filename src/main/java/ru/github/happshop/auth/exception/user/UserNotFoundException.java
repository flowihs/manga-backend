package ru.github.happshop.auth.exception.user;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.UserException;

public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super("Пользователь не был найден", HttpStatus.BAD_REQUEST);
    }
}
