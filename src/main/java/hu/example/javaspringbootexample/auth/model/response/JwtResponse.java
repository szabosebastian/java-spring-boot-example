package hu.example.javaspringbootexample.auth.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String accessToken;

    private String refreshToken;

    private String type = "Bearer";

    private UUID id;

    private String email;

    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken,  UUID id, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
