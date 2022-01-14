package com.sombra.promotion.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AbstractHttpStatusException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public NotFoundException(String message) {
        super(HTTP_STATUS, message);
    }

    public NotFoundException(String message, String technicalMessage) {
        super(HTTP_STATUS, message, technicalMessage);
    }

}
