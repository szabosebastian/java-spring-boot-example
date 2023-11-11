package hu.example.javaspringbootexample.auth.service;

import hu.example.javaspringbootexample.application.user.mapper.UserMapper;
import hu.example.javaspringbootexample.application.user.model.UserResponse;
import hu.example.javaspringbootexample.auth.data.RoleEntity;
import hu.example.javaspringbootexample.auth.data.RoleName;
import hu.example.javaspringbootexample.auth.data.UserEntity;
import hu.example.javaspringbootexample.auth.data.repository.RoleRepository;
import hu.example.javaspringbootexample.auth.data.repository.UserRepository;
import hu.example.javaspringbootexample.auth.model.request.LoginRequest;
import hu.example.javaspringbootexample.auth.model.request.SignupRequest;
import hu.example.javaspringbootexample.auth.model.response.JwtResponse;
import hu.example.javaspringbootexample.common.exception.BusinessValidationException;
import hu.example.javaspringbootexample.common.security.jwt.JwtUtils;
import hu.example.javaspringbootexample.common.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder encoder,
                       JwtUtils jwtUtils,
                       UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("User authenticated successfully!");
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles);
    }

    public UserResponse registerUser(SignupRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BusinessValidationException(HttpStatus.BAD_REQUEST, 400, "Error: Email is already in use!");
        }

        validateEmailFormat(signUpRequest.getEmail());

        // Create new user's account
        UserEntity userEntity = new UserEntity(
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<RoleName> strRoles = Set.of(RoleName.ROLE_USER); //TODO: roles setting
        Set<RoleEntity> roleEntities = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRoleEntity = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roleEntities.add(userRoleEntity);
        } else {
            strRoles.forEach(role -> {
                if (RoleName.ROLE_ADMIN.equals(role)) {
                    RoleEntity adminRoleEntity = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roleEntities.add(adminRoleEntity);
                } else {
                    RoleEntity userRoleEntity = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roleEntities.add(userRoleEntity);
                }
            });
        }

        userEntity.setRoleEntities(roleEntities);
        userEntity = userRepository.save(userEntity);

        log.info("User registered successfully!");
        return userMapper.mapToUserResponse(userEntity);
    }

    public void validateEmailFormat(String email) {
        if (email.contains(" ")) {
            throw new BusinessValidationException(HttpStatus.BAD_REQUEST, 400, "Email should not contain whitespace!");
        }
    }

    public ResponseEntity<?> getToken() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getEmail(),
                    roles));
        } catch (Exception e) {
            throw new BusinessValidationException(HttpStatus.FORBIDDEN, 403, "Cannot get user authentication!");
        }
    }
}
