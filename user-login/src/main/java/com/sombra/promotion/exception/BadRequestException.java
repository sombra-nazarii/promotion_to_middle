package com.sombra.promotion.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractHttpStatusException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(HTTP_STATUS, message);
    }

    public BadRequestException(String message, String technicalMessage) {
        super(HTTP_STATUS, message, technicalMessage);
    }

    public BadRequestException(String message, Throwable throwable) {
        super(HTTP_STATUS, message, throwable);
    }
}
