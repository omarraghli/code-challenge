package tanger.med.codechallenge.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tanger.med.codechallenge.domain.entity.User;

import java.util.Optional;

/**
 * JPA repository interface for managing User entity.
 * Extends JpaRepository to inherit basic CRUD operations and custom queries.
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user.
     * @return An {@link Optional} containing the user, or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return An {@link Optional} containing the user, or empty if not found.
     */
    Optional<User> findByUsername(String username);
}
