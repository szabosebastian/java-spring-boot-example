package hu.example.javaspringbootexample.application.idm.user.model.request;

import hu.example.javaspringbootexample.common.filter.type.StringFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFilter {

    @Schema(description = "User email filter")
    private StringFilter email;
}
