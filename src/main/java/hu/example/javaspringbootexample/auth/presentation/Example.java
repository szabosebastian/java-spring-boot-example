package hu.example.javaspringbootexample.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class Example {

    @GetMapping
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok(null);
    }
}
