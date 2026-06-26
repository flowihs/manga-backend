package ru.github.happshop.mail.exception;

import org.springframework.http.HttpStatus;
import ru.github.happshop.common.error.exception.MailException;

public class TemplateNotFoundException extends MailException {
    public TemplateNotFoundException() {
        super("Шаблон письма не был найден", HttpStatus.NOT_FOUND);
    }
}
