package tanger.med.codechallenge.application.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tanger.med.codechallenge.api.dtos.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dtos.AuthenticationResponseDTO;
import tanger.med.codechallenge.api.dtos.UserDTO;
import tanger.med.codechallenge.api.interfaces.AuthenticationService;
import tanger.med.codechallenge.domain.entities.Token;
import tanger.med.codechallenge.domain.entities.User;
import tanger.med.codechallenge.domain.enums.TokenType;
import tanger.med.codechallenge.domain.repositories.TokenRepo;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepo repository;
    private final TokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    //1
    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Optional<User> userByEmail = repository.findByEmail(request.getEmail());
            Optional<User> userByUsername = repository.findByUsername(request.getEmail());

            var user = userByEmail.orElseGet(() -> {
                if (userByUsername.isPresent()) {
                    return userByUsername.get(); // Assign the value found by username to the 'user' variable
                } else {
                    throw new UsernameNotFoundException("User not found by email or username");
                }
            });

            var jwtToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponseDTO.builder()
                    .accessToken(jwtToken)
                    .build();

        }

    @Override
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    //3
    @Override
    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    @Override
    public void register(UserDTO request) {
        var user = User.builder()
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
