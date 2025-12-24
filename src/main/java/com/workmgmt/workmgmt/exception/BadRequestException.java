package com.workmgmt.workmgmt.exception;

//package com.company.workmgmt.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

