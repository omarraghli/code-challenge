package tanger.med.codechallenge.api.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tanger.med.codechallenge.api.dto.ImportSummaryDTO;
import tanger.med.codechallenge.api.dto.UserDTO;
import tanger.med.codechallenge.config.exception.UserNotFoundException;
import tanger.med.codechallenge.domain.entity.User;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Interface defining operations related to user management and data generation.
 */
public interface UserService {

    /**
     * Generates a list of users with random data.
     *
     * @param count The number of users to generate.
     * @return A list of User objects with randomly generated data.
     */
    List<User> generateUsers(int count);

    /**
     * Downloads a JSON file containing randomly generated user data.
     *
     * @param count    The number of users to generate and include in the JSON file.
     * @param response The HttpServletResponse to set download headers.
     * @throws IOException If an I/O exception occurs during file writing.
     */
    void downloadUsersJson(int count, HttpServletResponse response) throws IOException;

    /**
     * Uploads a batch of users from a JSON file and returns an import summary.
     *
     * @param file The MultipartFile representing the JSON file with user data.
     * @return A ResponseEntity containing the import summary.
     * @throws IOException If an I/O exception occurs during file processing.
     */
    ResponseEntity<ImportSummaryDTO> uploadUsersBatch(MultipartFile file) throws IOException;

    /**
     * Retrieves a paginated list of all users.
     *
     * @param page    The page number.
     * @param size    The number of users per page.
     * @param request The HttpServletRequest containing authorization data.
     * @return A ResponseEntity containing a paginated list of UserDTO objects.
     */
    public ResponseEntity<Page<UserDTO>> getAllUsersOnlyAdmin(int page, int size, HttpServletRequest request);

    /**
     * Retrieves user details by email for administrators only.
     *
     * @param email   The email address of the user.
     * @param request The HttpServletRequest containing authorization data.
     * @return A ResponseEntity containing user details or an empty optional if not found.
     */
    ResponseEntity<Optional<UserDTO>> getUserByEmailOnlyAdmin(String email, HttpServletRequest request);

    /**
     * Retrieves user details by username for administrators only.
     *
     * @param username The username of the user.
     * @param request  The HttpServletRequest containing authorization data.
     * @return A ResponseEntity containing user details or an empty optional if not found.
     */
    ResponseEntity<Optional<UserDTO>> getUserByUsernameOnlyAdmin(String username, HttpServletRequest request);

    /**
     * Retrieves details of the currently authenticated user.
     *
     * @param request The HttpServletRequest containing authorization data.
     * @return A ResponseEntity containing user details or an empty optional if not found.
     */
    ResponseEntity<Optional<UserDTO>> getMyUser(HttpServletRequest request);

    /**
     * Checks if the user associated with the provided HttpServletRequest is an admin.
     *
     * @param request The HttpServletRequest containing authorization data.
     * @return {@code true} if the user is an admin, {@code false} otherwise.
     * @throws UserNotFoundException If the user associated with the request is not found.
     */
    public Boolean isAdmin(HttpServletRequest request) throws UserNotFoundException;

    /**
     * Retrieves user data based on the authorization data present in the HttpServletRequest.
     * This method assumes the use of JWT for authentication and extracts the user's email from the token.
     *
     * @param request The HttpServletRequest containing authorization data.
     * @return An Optional containing UserDTO if the user is found, or an empty Optional if not found.
     */
    Optional<UserDTO> retrieveUserDataByAuthorizationData(HttpServletRequest request);
}
