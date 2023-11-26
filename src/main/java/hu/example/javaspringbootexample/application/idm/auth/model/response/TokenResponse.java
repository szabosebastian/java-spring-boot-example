package hu.example.javaspringbootexample.application.idm.auth.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;
}
