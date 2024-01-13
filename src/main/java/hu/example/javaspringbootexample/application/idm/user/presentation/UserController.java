package hu.example.javaspringbootexample.application.idm.user.presentation;

import hu.example.javaspringbootexample.application.idm.auth.model.request.SignupExtendedRequest;
import hu.example.javaspringbootexample.application.idm.user.model.request.UserFilter;
import hu.example.javaspringbootexample.application.idm.user.model.request.UserUpdateRequest;
import hu.example.javaspringbootexample.application.idm.user.model.response.UserResponse;
import hu.example.javaspringbootexample.application.idm.user.service.UserService;
import hu.example.javaspringbootexample.common.message.PageResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
//@PreAuthorize(AuthRole.ADMIN_ROLE)
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    private UserService service;

    @GetMapping
    public PageResponse<UserResponse> listUsers(@ParameterObject UserFilter userFilter, @ParameterObject Pageable pageable) {
        System.out.println("Test");
        return service.listUsers(userFilter, pageable);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        return service.getUser(id);
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody SignupExtendedRequest signUpRequest) {
        return service.createUser(signUpRequest);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest updateRequest) {
        return service.updateUser(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        service.deleteUser(id);
    }
}
