package tanger.med.codechallenge.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tanger.med.codechallenge.config.security.CustomAuthenticationProvider;
import tanger.med.codechallenge.domain.entities.User;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import java.util.Optional;

/**
 * Configuration class for security-related settings.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final UserRepo repository;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    /**
     * Provides a BCryptPasswordEncoder bean for password encoding.
     *
     * @return A PasswordEncoder using the BCrypt hashing algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> userByEmail = repository.findByEmail(username);
            Optional<User> userByUsername = repository.findByUsername(username);
            User tmpUser = null;

            if (userByEmail.isPresent()){
                tmpUser = userByEmail.get();
            }

            if (userByUsername.isPresent()) {
                tmpUser = userByUsername.get();
            }

            return tmpUser;
        };
    }



    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(this.customAuthenticationProvider);

        return authenticationManagerBuilder.build();
    }


}
