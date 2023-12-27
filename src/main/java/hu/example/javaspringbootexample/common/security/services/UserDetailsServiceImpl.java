package hu.example.javaspringbootexample.common.security.services;

import hu.example.javaspringbootexample.application.idm.user.data.UserEntity;
import hu.example.javaspringbootexample.application.idm.user.data.repository.UserRepository;
import hu.example.javaspringbootexample.common.exception.BusinessValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Load by email
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this credential: " + email));

        return UserDetailsImpl.build(userEntity);
    }

    @Transactional
    public UserDetails loadUserByUserJwtSubject(UUID jwtSubject) {
        UserEntity userEntity = userRepository.findByJwtSubject(jwtSubject)
                .orElseThrow(() -> new BusinessValidationException(HttpStatus.FORBIDDEN, 403, "User not found for this JWT."));

        return UserDetailsImpl.build(userEntity);
    }
}
