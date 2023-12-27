package hu.example.javaspringbootexample.application.idm.user.service;

import hu.example.javaspringbootexample.application.idm.auth.model.request.SignupExtendedRequest;
import hu.example.javaspringbootexample.application.idm.auth.service.AuthService;
import hu.example.javaspringbootexample.application.idm.user.data.repository.UserRepository;
import hu.example.javaspringbootexample.application.idm.user.mapper.UserMapper;
import hu.example.javaspringbootexample.application.idm.user.model.request.UserFilter;
import hu.example.javaspringbootexample.application.idm.user.model.request.UserUpdateRequest;
import hu.example.javaspringbootexample.application.idm.user.model.response.UserResponse;
import hu.example.javaspringbootexample.common.exception.EntityNotFoundException;
import hu.example.javaspringbootexample.common.filter.FilterBuilder;
import hu.example.javaspringbootexample.common.message.PageResponse;
import hu.example.javaspringbootexample.common.message.PagingUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public UserResponse getUser(UUID id) {
        return mapper.mapToUserResponse(userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found: " + id)
        ));
    }

    public UserResponse createUser(SignupExtendedRequest request) {
        return authService.registerUser(request);
    }

    public UserResponse updateUser(UUID id, UserUpdateRequest updateRequest) {

        var userEntity = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found: " + id)
        );

        //TODO: validate if new email not exists already

        userEntity.setEmail(updateRequest.getEmail());

        //TODO: role update

        return null;
    }

    public void deleteUser(UUID id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found: " + id)
        ));
    }
}
