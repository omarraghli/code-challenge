package tanger.med.codechallenge.domain.repositories;

import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tanger.med.codechallenge.domain.entities.User;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository interface for managing User entities.
 * Extends JpaRepository to inherit basic CRUD operations and custom queries.
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUsername(String username);

}
