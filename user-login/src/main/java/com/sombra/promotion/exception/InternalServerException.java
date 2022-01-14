package com.sombra.promotion.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends AbstractHttpStatusException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public InternalServerException(String message) {
        super(HTTP_STATUS, message);
    }

    public InternalServerException(String message, String technicalMessage) {
        super(HTTP_STATUS, message, technicalMessage);
    }

    public InternalServerException(String message, Throwable throwable) {
        super(HTTP_STATUS, message, throwable);
    }
}
