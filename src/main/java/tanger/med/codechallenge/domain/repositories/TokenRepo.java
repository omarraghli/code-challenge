package tanger.med.codechallenge.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tanger.med.codechallenge.domain.entity.Token;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Token} entity in the database.
 */
@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {

    /**
     * Retrieves a list of all valid tokens associated with a specific user.
     *
     * @param id The ID of the user.
     * @return A list of valid tokens for the specified user.
     */
    @Query(value = """
      select t from Token t inner join User u
      on t.user.id = u.id
      where u.id = :id and (t.expired = false or t.revoked = false)
      """)
    List<Token> findAllValidTokenByUser(long id);

    /**
     * Retrieves a token by its value.
     *
     * @param token The value of the token.
     * @return An {@link Optional} containing the token, or empty if not found.
     */
    Optional<Token> findByToken(String token);
}
