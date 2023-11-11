package hu.example.javaspringbootexample.auth.presentation;

import hu.example.javaspringbootexample.application.user.model.UserResponse;
import hu.example.javaspringbootexample.auth.model.request.LoginRequest;
import hu.example.javaspringbootexample.auth.model.request.SignupRequest;
import hu.example.javaspringbootexample.auth.model.response.JwtResponse;
import hu.example.javaspringbootexample.auth.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public UserResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken() {
        return authService.getToken();
    }
}
