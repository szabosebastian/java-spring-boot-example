package hu.example.javaspringbootexample.auth.presentation;

import hu.example.javaspringbootexample.application.user.model.UserResponse;
import hu.example.javaspringbootexample.auth.model.request.LoginRequest;
import hu.example.javaspringbootexample.auth.model.request.SignupRequest;
import hu.example.javaspringbootexample.auth.model.response.JwtResponse;
import hu.example.javaspringbootexample.auth.model.response.TokenResponse;
import hu.example.javaspringbootexample.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        return new ResponseEntity<>(authService.refreshToken(request), HttpStatus.OK);
    }

}
