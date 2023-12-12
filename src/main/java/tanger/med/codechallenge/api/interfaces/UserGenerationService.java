package tanger.med.codechallenge.api.interfaces;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tanger.med.codechallenge.api.dtos.ImportSummaryDTO;
import tanger.med.codechallenge.domain.entities.User;

import java.io.IOException;
import java.util.List;

/**
 * Interface defining operations related to user management and data generation.
 */
public interface UserGenerationService {

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
}
