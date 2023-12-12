package tanger.med.codechallenge.application.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tanger.med.codechallenge.api.dtos.ImportSummaryDTO;
import tanger.med.codechallenge.application.services.UserGenerationServiceImpl;

import java.io.IOException;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users") // Global path for user-related endpoints
public class UserGenerationController {

    private final UserGenerationServiceImpl userGenerationServiceImpl;



    /**
     * Endpoint for generating and downloading a JSON file containing random users.
     *
     * @param count    The number of users to generate.
     * @param response The HttpServletResponse to set download headers.
     * @throws IOException If an I/O exception occurs during file writing.
     */
    @GetMapping("/generate")
    public void generateAndDownloadUsers(@RequestParam(name = "count", defaultValue = "100") int count,
                                         HttpServletResponse response) throws IOException {
        userGenerationServiceImpl.downloadUsersJson(count, response);
    }

    /**
     * Endpoint for uploading a JSON file containing user data in batch and returning an import summary.
     *
     * @param file The MultipartFile representing the JSON file with user data.
     * @return A ResponseEntity containing the import summary.
     * @throws IOException If an I/O exception occurs during file processing.
     */
    @PostMapping("/batch")
    public ResponseEntity<ImportSummaryDTO> uploadUserFile(@RequestParam("file") MultipartFile file) throws IOException {
        return userGenerationServiceImpl.uploadUsersBatch(file);
    }
}
