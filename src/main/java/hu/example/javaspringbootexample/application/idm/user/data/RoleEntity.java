package hu.example.javaspringbootexample.application.idm.user.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    @Column(name = "ID", nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RoleName name;

    @ManyToMany(mappedBy = "roleEntities")
    Set<UserEntity> userEntities;
}
