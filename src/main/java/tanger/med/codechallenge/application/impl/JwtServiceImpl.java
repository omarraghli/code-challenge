package tanger.med.codechallenge.application.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tanger.med.codechallenge.api.service.JwtService;
import tanger.med.codechallenge.domain.entity.User;
import tanger.med.codechallenge.domain.repositories.UserRepo;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service implementation for JSON Web Token (JWT) related operations.
 */
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final UserRepo userRepo;


    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the JWT token.
     *
     * @param token         The JWT token.
     * @param claimResolver The function to resolve the claim from the token's claims.
     * @param <T>           The type of the claim.
     * @return The resolved claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token.
     * @return The claims extracted from the token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gets the signing key for the JWT token.
     *
     * @return The signing key.
     */
    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Builds a JWT token with specified claims, user details, and expiration.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The UserDetails representing the user.
     * @param expiration  The expiration time of the token.
     * @return The generated JWT token.
     */
    @Override
    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if a token is valid for a given user.
     *
     * @param token       The JWT token.
     * @param userDetails The UserDetails representing the user.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = userDetails.getUsername();
        if (isValidEmail(username)) {
            Optional<User> tmpUser = this.userRepo.findByEmail(username);
            if (tmpUser.isPresent()) {
                username = tmpUser.get().getUsername();
            }
        }
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if a token is expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Checks if an email is in a valid format.
     *
     * @param email The email address to validate.
     * @return True if the email is valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        // Define the regular expression for a valid email address
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);

        // Return true if the email matches the pattern, false otherwise
        return matcher.matches();
    }

    /**
     * Generates a JWT token for a user based on user details.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The UserDetails representing the user.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String payload = userDetails.getUsername();
        if (!isValidEmail(payload)) {
            Optional<User> tmpUser = this.userRepo.findByUsername(payload);
            if (tmpUser.isPresent()) {
                payload = tmpUser.get().getEmail();
            }
        }
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(payload)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JWT token for a user.
     *
     * @param userDetails The UserDetails representing the user.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
}
