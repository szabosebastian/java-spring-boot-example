package hu.example.javaspringbootexample.common.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends CommonException {
    public EntityNotFoundException(HttpStatus status, int statusCode, String statusMessage) {
        super(status, statusCode, statusMessage);
    }

    public EntityNotFoundException(String statusMessage) {
        super(HttpStatus.NOT_FOUND, 404, statusMessage);
    }
}
