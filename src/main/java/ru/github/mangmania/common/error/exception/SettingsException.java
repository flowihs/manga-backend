package ru.github.mangmania.common.error.exception;

import org.springframework.http.HttpStatus;

public class SettingsException extends RuntimeException {
    private final HttpStatus status;

    public SettingsException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
