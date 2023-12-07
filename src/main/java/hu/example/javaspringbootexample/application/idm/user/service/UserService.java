package hu.example.javaspringbootexample.application.idm.user.service;

import hu.example.javaspringbootexample.application.idm.auth.model.request.SignupExtendedRequest;
import hu.example.javaspringbootexample.application.idm.auth.service.AuthService;
import hu.example.javaspringbootexample.application.idm.user.data.repository.UserRepository;
import hu.example.javaspringbootexample.application.idm.user.mapper.UserMapper;
import hu.example.javaspringbootexample.application.idm.user.model.UserFilter;
import hu.example.javaspringbootexample.application.idm.user.model.UserResponse;
import hu.example.javaspringbootexample.common.filter.FilterBuilder;
import hu.example.javaspringbootexample.common.message.PageResponse;
import hu.example.javaspringbootexample.common.message.PagingUtil;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final AuthService authService;
    private UserRepository userRepository;
    private UserMapper mapper;

    public PageResponse<UserResponse> listUsers(UserFilter userFilter, Pageable pageable) {
        var users = userRepository.findAll(FilterBuilder.fromObject(userFilter), pageable);
        return PagingUtil.getPageResponse(users, mapper::mapToUserResponse);
    }

    public UserResponse createUser(SignupExtendedRequest request) {
        return authService.registerUser(request);
    }
}
