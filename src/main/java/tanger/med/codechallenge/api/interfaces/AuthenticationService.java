package tanger.med.codechallenge.api.interfaces;

import tanger.med.codechallenge.api.dtos.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dtos.AuthenticationResponseDTO;
import tanger.med.codechallenge.api.dtos.UserDTO;
import tanger.med.codechallenge.domain.entities.User;

/**
 * Interface defining authentication-related operations.
 * <p>
 * This interface provides methods for authentication, saving user tokens,
 * revoking user tokens, and user registration.
 */
public interface AuthenticationService {

    /**
     * Authenticate a user based on the provided authentication request.
     *
     * @param request The authentication request containing user credentials.
     * @return An authentication response containing an access token.
     */
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);

    /**
     * Save the user token for the given user.
     *
     * @param user     The user for whom the token needs to be saved.
     * @param jwtToken The JWT token to be saved.
     */
    void saveUserToken(User user, String jwtToken);

    /**
     * Revoke all tokens for the given user.
     *
     * @param user The user for whom all tokens should be revoked.
     */
    void revokeAllUserTokens(User user);

    /**
     * Register a new user based on the provided user data.
     *
     * @param request The user data for registration.
     */
    void register(UserDTO request);
}
