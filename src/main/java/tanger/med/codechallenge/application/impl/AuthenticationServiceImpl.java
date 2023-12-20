package tanger.med.codechallenge.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tanger.med.codechallenge.api.dto.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dto.AuthenticationResponseDTO;
import tanger.med.codechallenge.api.dto.UserDTO;
import tanger.med.codechallenge.api.service.AuthenticationService;
import tanger.med.codechallenge.config.exception.UserNotFoundException;
import tanger.med.codechallenge.domain.entity.Token;
import tanger.med.codechallenge.domain.entity.User;
import tanger.med.codechallenge.domain.enums.TokenType;
import tanger.med.codechallenge.domain.repositories.TokenRepo;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for authentication-related operations.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo repository;
    private final TokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user based on the provided credentials and generates an access token.
     *
     * @param request The authentication request containing user credentials.
     * @return An authentication response containing the generated access token.
     * @throws UsernameNotFoundException If the user is not found by email or username.
     */
    @Override
    public ResponseEntity<AuthenticationResponseDTO> authenticate(AuthenticationRequestDTO request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Optional<User> userByEmail = repository.findByEmail(request.getEmail());
        Optional<User> userByUsername = repository.findByUsername(request.getEmail());

        var user = userByEmail.orElseGet(() -> {
            if (userByUsername.isPresent()) {
                return userByUsername.get(); // Assign the value found by username to the 'user' variable
            } else {
                throw new UserNotFoundException("User not found by email or username");
            }
        });



        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        AuthenticationResponseDTO authenticationDTO = AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .build();

        return ResponseEntity.ok(authenticationDTO);
    }

    /**
     * Revokes all valid tokens for a given user.
     *
     * @param user The user for whom tokens should be revoked.
     */
    @Override
    public void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * Saves a user token to the database.
     *
     * @param user     The user for whom the token is saved.
     * @param jwtToken The JWT token to be saved.
     */
    @Override
    public void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * Registers a new user in the system.
     *
     * @param request The user registration request containing user details.
     */
    @Override
    public void register(UserDTO request) {
        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .company(request.getCompany())
                .country(request.getCountry())
                .mobile(request.getMobile())
                .city(request.getCity())
                .birthDate(request.getBirthDate())
                .jobPosition(request.getJobPosition())
                .avatar(request.getAvatar())
                .role(request.getRole())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(user);
    }
}
