package hu.example.javaspringbootexample.application.idm.auth.model.request;

import hu.example.javaspringbootexample.application.idm.user.data.RoleName;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SignupExtendedRequest extends SignupRequest {

    public SignupExtendedRequest(SignupRequest request, Set<RoleName> roles) {
        super(request);
        this.roles = roles;
    }

    private Set<RoleName> roles;
}
