package hu.example.javaspringbootexample.application.idm.auth.presentation;

import hu.example.javaspringbootexample.application.idm.user.model.response.UserResponse;
import hu.example.javaspringbootexample.application.idm.auth.model.request.LoginRequest;
import hu.example.javaspringbootexample.application.idm.auth.model.request.SignupRequest;
import hu.example.javaspringbootexample.application.idm.auth.model.response.JwtResponse;
import hu.example.javaspringbootexample.application.idm.auth.model.response.TokenResponse;
import hu.example.javaspringbootexample.application.idm.auth.service.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping(value = "/signin")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authService.authenticateUser(loginRequest, response);
    }


    @PostMapping("/signup")
    public UserResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerPublicUser(signUpRequest);
    }

    //TODO: COOKIE
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        return new ResponseEntity<>(authService.refreshToken(request), HttpStatus.OK);
    }
}
