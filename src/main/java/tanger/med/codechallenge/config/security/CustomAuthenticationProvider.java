package tanger.med.codechallenge.config.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tanger.med.codechallenge.application.services.UserServiceImpl;
import tanger.med.codechallenge.domain.entities.User;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserRepo userRepo;

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

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
