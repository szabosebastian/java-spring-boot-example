package hu.example.javaspringbootexample.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CommonException extends RuntimeException {
    protected HttpStatus status;

    protected int statusCode;

    protected String statusMessage;

    public CommonException(HttpStatus status, int statusCode, String statusMessage) {
        this.status = status;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
