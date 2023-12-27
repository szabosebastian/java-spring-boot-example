package hu.example.javaspringbootexample.application.idm.user.model.response;

import hu.example.javaspringbootexample.application.idm.user.data.RoleName;
import hu.example.javaspringbootexample.common.message.Response;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Response {

    @NotNull
    private UUID id;
    private String email;
    private List<RoleName> roles;
}
