package tanger.med.codechallenge.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tanger.med.codechallenge.api.dtos.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dtos.AuthenticationResponseDTO;
import tanger.med.codechallenge.api.dtos.RegisterRequestDTO;
import tanger.med.codechallenge.api.interfaces.AuthenticationService;
import tanger.med.codechallenge.api.interfaces.JwtService;
import tanger.med.codechallenge.domain.entities.Token;
import tanger.med.codechallenge.domain.entities.User;
import tanger.med.codechallenge.domain.enums.TokenType;
import tanger.med.codechallenge.domain.repositories.TokenRepo;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepo repository;
    private final TokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    //debug
    private final UserRepo userRepo;

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {

        Optional<User> u = userRepo.findByEmail(request.getEmail());
        System.out.println(u.toString());

        System.out.println("Before authentication");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        System.out.println("After authentication");


        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        System.out.println(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

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
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {


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

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


}
