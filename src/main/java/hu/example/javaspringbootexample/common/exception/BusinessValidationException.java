package hu.example.javaspringbootexample.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessValidationException extends CommonException {
    public BusinessValidationException(HttpStatus status, int statusCode, String statusMessage) {
        super(status, statusCode, statusMessage);
    }
}
