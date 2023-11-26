package hu.example.javaspringbootexample.application.idm.user.data.repository;

import hu.example.javaspringbootexample.application.idm.user.data.RoleEntity;
import hu.example.javaspringbootexample.application.idm.user.data.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByName(RoleName name);
}
