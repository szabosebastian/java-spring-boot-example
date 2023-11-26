package hu.example.javaspringbootexample.application.idm.user.presentation;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserRepository userRepository;
    private UserMapper mapper;

    @GetMapping
    PageResponse<UserResponse> listUsers(@ParameterObject UserFilter userFilter, @ParameterObject Pageable pageable) {
        var users = userRepository.findAll(FilterBuilder.fromObject(userFilter), pageable);
        return PagingUtil.getPageResponse(users, mapper::mapToUserResponse);
    }
}
