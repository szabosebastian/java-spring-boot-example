package hu.example.javaspringbootexample.application.user.mapper;

import hu.example.javaspringbootexample.application.user.model.UserResponse;
import hu.example.javaspringbootexample.auth.data.RoleEntity;
import hu.example.javaspringbootexample.auth.data.RoleName;
import hu.example.javaspringbootexample.auth.data.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roleEntities", qualifiedByName = "mapRoleEntityToRoleList")
    UserResponse mapToUserResponse(UserEntity userEntity);

    @Named("mapRoleEntityToRoleList")
    default List<RoleName> mapRoleEntityToRoleList(Set<RoleEntity> roles) {
        return roles.stream().map(RoleEntity::getName).collect(Collectors.toList());
    }
}
