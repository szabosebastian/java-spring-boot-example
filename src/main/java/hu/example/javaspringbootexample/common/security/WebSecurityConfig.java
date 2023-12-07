package hu.example.javaspringbootexample.common.security;

import hu.example.javaspringbootexample.common.security.jwt.AuthEntryPointJwt;
import hu.example.javaspringbootexample.common.security.jwt.AuthTokenFilter;
import hu.example.javaspringbootexample.common.security.jwt.JwtUtils;
import hu.example.javaspringbootexample.common.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors((cors) -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowedOrigins(List.of("*"));
                            configuration.setAllowedMethods(List.of("*"));
                            configuration.setAllowedHeaders(List.of("*"));
                            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                            source.registerCorsConfiguration("/**", configuration);
                            cors.configurationSource(source);
                        }
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
//                            .requestMatchers(HttpMethod.GET, "/event", "/news", "/password").permitAll() //TODO
//                            .requestMatchers(HttpMethod.POST, "/password/**").permitAll() //TODO
                            .requestMatchers(HttpMethod.GET, "/user", "/user/**").permitAll() //TODO
                            .requestMatchers(HttpMethod.POST, "/user", "/user/**").permitAll() //TODO kivenni
                            .requestMatchers("/auth/**", "/swagger-resources/**", "/swagger-ui/**", "/webjars/springfox-swagger-ui/**", "/v2/api-docs**", "/v3/api-docs**", "/v3/api-docs/**", "/swagger**").permitAll()
                            .requestMatchers("/error**").permitAll()
                            .anyRequest().authenticated();
                });

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
