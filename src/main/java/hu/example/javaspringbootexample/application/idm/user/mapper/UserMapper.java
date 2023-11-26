package hu.example.javaspringbootexample.application.idm.user.mapper;

import hu.example.javaspringbootexample.application.idm.user.model.UserResponse;
import hu.example.javaspringbootexample.application.idm.user.data.RoleEntity;
import hu.example.javaspringbootexample.application.idm.user.data.RoleName;
import hu.example.javaspringbootexample.application.idm.user.data.UserEntity;
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
