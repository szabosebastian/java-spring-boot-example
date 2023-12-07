package hu.example.javaspringbootexample.application.idm.user.presentation;

import hu.example.javaspringbootexample.application.idm.auth.model.AuthRole;
import hu.example.javaspringbootexample.application.idm.auth.model.request.SignupExtendedRequest;
import hu.example.javaspringbootexample.application.idm.user.data.repository.UserRepository;
import hu.example.javaspringbootexample.application.idm.user.mapper.UserMapper;
import hu.example.javaspringbootexample.application.idm.user.model.UserFilter;
import hu.example.javaspringbootexample.application.idm.user.model.UserResponse;
import hu.example.javaspringbootexample.application.idm.user.service.UserService;
import hu.example.javaspringbootexample.common.filter.FilterBuilder;
import hu.example.javaspringbootexample.common.message.PageResponse;
import hu.example.javaspringbootexample.common.message.PagingUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserService service;

    @PreAuthorize(AuthRole.ADMIN_ROLE)
    @GetMapping
    PageResponse<UserResponse> listUsers(@ParameterObject UserFilter userFilter, @ParameterObject Pageable pageable) {
        return service.listUsers(userFilter, pageable);
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody SignupExtendedRequest signUpRequest) {
        return service.createUser(signUpRequest);
    }
}
