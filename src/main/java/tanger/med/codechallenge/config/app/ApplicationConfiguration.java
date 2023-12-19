package tanger.med.codechallenge.config.app;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tanger.med.codechallenge.config.security.CustomAuthenticationProvider;
import tanger.med.codechallenge.domain.entity.User;
import tanger.med.codechallenge.domain.repositories.UserRepo;
import java.util.Optional;

/**
 * Configuration class for security-related settings.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration{


    private final UserRepo repository;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Value("${front.domain}")
    private String frontDomain;
    /**
     * Provides a BCryptPasswordEncoder bean for password encoding.
     *
     * @return A PasswordEncoder using the BCrypt hashing algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a UserDetailsService bean for loading user details by username.
     *
     * @return A UserDetailsService bean.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> userByEmail = repository.findByEmail(username);
            Optional<User> userByUsername = repository.findByUsername(username);
            User tmpUser = null;

            if (userByEmail.isPresent()) {
                tmpUser = userByEmail.get();
            }

            if (userByUsername.isPresent()) {
                tmpUser = userByUsername.get();
            }

            if (tmpUser == null) {
                throw new UsernameNotFoundException("User not found");
            }

            return tmpUser;
        };
    }

    /**
     * Provides an AuthenticationProvider bean for authentication.
     *
     * @return An AuthenticationProvider bean.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides an AuthenticationManager bean for authentication.
     *
     * @param http The HttpSecurity object for configuring security settings.
     * @return An AuthenticationManager bean.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(this.customAuthenticationProvider);

        return authenticationManagerBuilder.build();
    }
}