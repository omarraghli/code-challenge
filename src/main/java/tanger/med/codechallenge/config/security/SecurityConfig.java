package tanger.med.codechallenge.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for security-related settings.
 */
@Configuration
public class SecurityConfig {

    /**
     * Provides a BCryptPasswordEncoder bean for password encoding.
     *
     * @return A PasswordEncoder using the BCrypt hashing algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
