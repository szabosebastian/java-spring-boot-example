package hu.example.javaspringbootexample.application.user.model;

import hu.example.javaspringbootexample.auth.data.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    @NotNull
    private UUID id;
    private String email;
    private List<RoleName> roles;
}
