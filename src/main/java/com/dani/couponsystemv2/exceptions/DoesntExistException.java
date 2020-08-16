package com.dani.couponsystemv2.exceptions;

public class DoesntExistException extends Exception {
    public DoesntExistException() {
    }

    public DoesntExistException(String message) {
        super(message);
    }

    public DoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoesntExistException(Throwable cause) {
        super(cause);
    }

    public DoesntExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
