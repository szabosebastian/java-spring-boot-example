package hu.example.javaspringbootexample.common.exception.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalExceptionErrorDTO {

    private Instant timestamp;

    private int status;

    private String error;
}
