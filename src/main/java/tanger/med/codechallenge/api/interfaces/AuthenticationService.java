package tanger.med.codechallenge.api.interfaces;
import org.springframework.data.domain.Page;

import tanger.med.codechallenge.api.dtos.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dtos.AuthenticationResponseDTO;
import tanger.med.codechallenge.api.dtos.RegisterRequestDTO;
import tanger.med.codechallenge.domain.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AuthenticationService {

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
    public void saveUserToken(User user, String jwtToken);

    public void revokeAllUserTokens(User user);

    public void register(RegisterRequestDTO request);
}
