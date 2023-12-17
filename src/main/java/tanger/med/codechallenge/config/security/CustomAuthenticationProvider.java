package tanger.med.codechallenge.config.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tanger.med.codechallenge.domain.entity.User;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import java.util.Optional;

/**
 * Custom authentication provider for authenticating users based on email or username and password.
 * <p>
 * This class implements the Spring Security AuthenticationProvider interface
 * to provide custom authentication logic for the application.
 */
@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepo userRepo;

    /**
     * Authenticate the user based on the provided authentication object.
     * <p>
     * The authentication is performed based on either email or username and password.
     *
     * @param authentication The authentication object containing user credentials.
     * @return A fully authenticated Authentication object if authentication is successful.
     * @throws AuthenticationException If authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> user = userRepo.findByEmail(email);

        if (user.isEmpty()) {
            user = userRepo.findByUsername(email);

            if (user.isEmpty())
                throw new UsernameNotFoundException("User not found");
        }

        if (!password.equals(user.get().getPassword())) {
            throw new AuthenticationException("Invalid credentials") {
            };
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.get().getAuthorities());
    }

    /**
     * Check if this AuthenticationProvider supports the provided authentication type.
     *
     * @param authentication The authentication class to check for support.
     * @return True if this AuthenticationProvider supports the provided authentication class, false otherwise.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
