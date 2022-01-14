package com.sombra.promotion.exception;

import org.springframework.http.HttpStatus;

public class AbstractHttpStatusException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String technicalMessage;

    protected AbstractHttpStatusException(final HttpStatus httpStatus, final String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.technicalMessage = "";
    }

    protected AbstractHttpStatusException(final HttpStatus httpStatus, final String message, final Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.technicalMessage = "";
    }

    protected AbstractHttpStatusException(HttpStatus httpStatus, String message, String technicalMessage) {
        super(message);
        this.technicalMessage = technicalMessage;
        this.httpStatus = httpStatus;
    }

    public String getTechnicalMessage() {
        return technicalMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
