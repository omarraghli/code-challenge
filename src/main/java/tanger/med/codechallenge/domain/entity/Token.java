package tanger.med.codechallenge.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tanger.med.codechallenge.domain.enums.TokenType;

/**
 * Entity class representing a security token associated with a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

    /**
     * The unique identifier for the token.
     */
    @Id
    @GeneratedValue
    public long id;

    /**
     * The token string.
     */
    public String token;

    /**
     * The type of the token (e.g., Bearer).
     */
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    /**
     * Indicates whether the token has been revoked.
     */
    public boolean revoked;

    /**
     * Indicates whether the token has expired.
     */
    public boolean expired;

    /**
     * The user associated with the token.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
