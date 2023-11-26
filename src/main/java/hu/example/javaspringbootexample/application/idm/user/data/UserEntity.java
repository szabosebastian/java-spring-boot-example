package hu.example.javaspringbootexample.application.idm.user.data;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "EMAIL")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    @Column(name = "ID", nullable = false, unique = true)
    private UUID id;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<RoleEntity> roleEntities = new HashSet<>();

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
