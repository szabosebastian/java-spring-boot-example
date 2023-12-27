package hu.example.javaspringbootexample.application.idm.user.model.request;

import hu.example.javaspringbootexample.application.idm.user.data.RoleName;
import hu.example.javaspringbootexample.common.message.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest implements Request {

    private String email;
    private Set<RoleName> roles;
}
