package com.epam.ems.web.hateoas.exception;

public class HateoasException extends RuntimeException{
    public HateoasException() {
        super();
    }

    public HateoasException(String message) {
        super(message);
    }

    public HateoasException(String message, Throwable cause) {
        super(message, cause);
    }

    public HateoasException(Throwable cause) {
        super(cause);
    }

    protected HateoasException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
