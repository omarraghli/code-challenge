package tanger.med.codechallenge.api.interfaces;

import tanger.med.codechallenge.api.dtos.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dtos.AuthenticationResponseDTO;
import tanger.med.codechallenge.api.dtos.UserDTO;
import tanger.med.codechallenge.domain.entities.User;

public interface AuthenticationService {

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
    public void saveUserToken(User user, String jwtToken);

    public void revokeAllUserTokens(User user);

    public void register(UserDTO request);
}
