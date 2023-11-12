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
import hu.example.javaspringbootexample.auth.model.response.TokenResponse;
import hu.example.javaspringbootexample.common.exception.BusinessValidationException;
import hu.example.javaspringbootexample.common.security.jwt.JwtUtils;
import hu.example.javaspringbootexample.common.security.services.UserDetailsImpl;
import hu.example.javaspringbootexample.common.security.services.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder encoder,
                       JwtUtils jwtUtils,
                       UserMapper userMapper,
                       UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.userDetailsService = userDetailsService;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateAccessJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("User authenticated successfully!");
        return new JwtResponse(
                accessToken,
                refreshToken,
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

    public TokenResponse refreshToken(HttpServletRequest request) {
        final String header = request.getHeader(AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            throw new BusinessValidationException(HttpStatus.BAD_REQUEST, 400, "No authorization header found!");
        }

        final String token = header.split(" ")[1].trim();

        if (!jwtUtils.validateJwtToken(token)) {
            throw new BusinessValidationException(HttpStatus.BAD_REQUEST, 400, "Refresh token is not valid!");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtils.geSubjectFromJwtToken(token));

        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

        String refreshToken = jwtUtils.generateRefreshJwtToken(authenticationToken);

        while (refreshToken.equals(token)) {
            refreshToken = jwtUtils.generateRefreshJwtToken(authenticationToken);
        }

        String accessToken = jwtUtils.generateAccessJwtToken(authenticationToken);

        return new TokenResponse(accessToken, refreshToken);
    }
}
