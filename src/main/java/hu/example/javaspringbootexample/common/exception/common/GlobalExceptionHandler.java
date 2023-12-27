package hu.example.javaspringbootexample.common.exception.common;

import hu.example.javaspringbootexample.common.exception.BusinessValidationException;
import hu.example.javaspringbootexample.common.exception.CommonException;
import hu.example.javaspringbootexample.common.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessValidationException.class)
    public final ResponseEntity<GlobalExceptionErrorDTO> handleBusinessValidationException(BusinessValidationException e) {
        return getAndLogExceptionResponse(e);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<GlobalExceptionErrorDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        return getAndLogExceptionResponse(e);
    }

    private ResponseEntity<GlobalExceptionErrorDTO> getAndLogExceptionResponse(CommonException e) {
        GlobalExceptionErrorDTO error = new GlobalExceptionErrorDTO(
                Instant.now(),
                e.getStatusCode(),
                e.getStatusMessage());
        //log.error(getStackTraceToString(e)); TODO: enable console log for handled exceptions if needed
        return new ResponseEntity<>(error, e.getStatus());
    }

    private String getStackTraceToString(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
