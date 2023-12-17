package tanger.med.codechallenge.api.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Interface defining operations related to JSON Web Tokens (JWT).
 * <p>
 * This interface provides methods for extracting information from JWTs,
 * generating JWTs, validating JWTs, and managing token expiration.
 */
public interface JwtService {

    /**
     * Extract the username from the provided JWT.
     *
     * @param token The JWT from which to extract the username.
     * @return The username extracted from the JWT.
     */
    public String extractUsername(String token);

    /**
     * Extract a specific claim from the provided JWT using a custom claim resolver function.
     *
     * @param token          The JWT from which to extract the claim.
     * @param claimResolver  The function to resolve the desired claim from the JWT claims.
     * @param <T>            The type of the extracted claim.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver);

    /**
     * Generate a JWT with additional claims for the given user details.
     *
     * @param extraClaims    Additional claims to be included in the JWT.
     * @param userDetails    The user details for whom the JWT is generated.
     * @return The generated JWT.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Generate a JWT for the given user details.
     *
     * @param userDetails    The user details for whom the JWT is generated.
     * @return The generated JWT.
     */
    public String generateToken(UserDetails userDetails);

    /**
     * Check if the provided JWT is valid for the given user details.
     *
     * @param token          The JWT to be validated.
     * @param userDetails    The user details for validation.
     * @return True if the JWT is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Check if the provided JWT has expired.
     *
     * @param token          The JWT to check for expiration.
     * @return True if the JWT has expired, false otherwise.
     */
    public boolean isTokenExpired(String token);

    /**
     * Extract the expiration date from the provided JWT.
     *
     * @param token          The JWT from which to extract the expiration date.
     * @return The expiration date extracted from the JWT.
     */
    public Date extractExpiration(String token);

    /**
     * Extract all claims from the provided JWT.
     *
     * @param token          The JWT from which to extract all claims.
     * @return All claims extracted from the JWT.
     */
    public Claims extractAllClaims(String token);

    /**
     * Get the signing key used for JWT validation.
     *
     * @return The signing key.
     */
    public Key getSignInKey();


    /**
     * Build a JWT with additional claims for the given user details and expiration duration.
     *
     * @param extraClaims    Additional claims to be included in the JWT.
     * @param userDetails    The user details for whom the JWT is generated.
     * @param expiration     The expiration duration for the JWT.
     * @return The built JWT.
     */
    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration);
}
