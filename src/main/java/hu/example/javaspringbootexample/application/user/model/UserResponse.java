package hu.example.javaspringbootexample.application.user.model;

import hu.example.javaspringbootexample.auth.data.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String email;
    private List<RoleName> roles;
}
