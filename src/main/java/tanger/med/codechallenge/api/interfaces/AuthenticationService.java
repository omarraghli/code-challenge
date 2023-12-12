package tanger.med.codechallenge.api.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tanger.med.codechallenge.api.dtos.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dtos.AuthenticationResponseDTO;
import tanger.med.codechallenge.api.dtos.RegisterRequestDTO;
import tanger.med.codechallenge.domain.entities.User;

import java.io.IOException;

public interface AuthenticationService {

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
    public void saveUserToken(User user, String jwtToken);

    public void revokeAllUserTokens(User user);

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    public AuthenticationResponseDTO register(RegisterRequestDTO request);
}
