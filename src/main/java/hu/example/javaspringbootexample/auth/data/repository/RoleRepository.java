package hu.example.javaspringbootexample.auth.data.repository;

import hu.example.javaspringbootexample.auth.data.RoleEntity;
import hu.example.javaspringbootexample.auth.data.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByName(RoleName name);
}
