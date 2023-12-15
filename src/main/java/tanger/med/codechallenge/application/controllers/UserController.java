package tanger.med.codechallenge.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tanger.med.codechallenge.api.dtos.ImportSummaryDTO;
import tanger.med.codechallenge.api.dtos.UserDTO;
import tanger.med.codechallenge.application.services.UserServiceImpl;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users") // Global path for user-related endpoints
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final UserRepo userRepo;

    /**
     * Endpoint for generating and downloading a JSON file containing random users.
     *
     * @param count    The number of users to generate.
     * @param response The HttpServletResponse to set download headers.
     * @throws IOException If an I/O exception occurs during file writing.
     */
    @GetMapping("/generate")
    @Operation(
            summary = "Generate and download a JSON file with random users",
            description = "This endpoint generates and downloads a JSON file containing random users."
    )
    public void generateAndDownloadUsers(@RequestParam(name = "count", defaultValue = "100") int count,
                                         HttpServletResponse response) throws IOException {
        userServiceImpl.downloadUsersJson(count, response);
    }

    /**
     * Endpoint for uploading a JSON file containing user data in batch and returning an import summary.
     *
     * @param file The MultipartFile representing the JSON file with user data.
     * @return A ResponseEntity containing the import summary.
     * @throws IOException If an I/O exception occurs during file processing.
     */
    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a JSON file with user data in batch",
            description = "This endpoint allows batch uploading of user data from a JSON file and returns an import summary."
    )
    public ResponseEntity<ImportSummaryDTO> uploadUserFile(@RequestPart("file") MultipartFile file) throws IOException {
        return userServiceImpl.uploadUsersBatch(file);
    }

    /**
     * Retrieves a paginated list of users.
     *
     * @param page The page number (zero-based).
     * @param size The number of users per page.
     * @return A Page containing UserDTOs.
     */
    @GetMapping("/getUsers")
    @Operation(
            summary = "Retrieve paginated list of users",
            description = "This endpoint retrieves a paginated list of users."
    )
    public Page<UserDTO> getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        return this.userServiceImpl.getAllUsers(page, size);
    }

    /**
     * Retrieves a user by email for administrators.
     *
     * @param email   The email of the user to retrieve.
     * @param request The HttpServletRequest.
     * @return A ResponseEntity containing an Optional<UserDTO>.
     */
    @GetMapping("/email/{email}")
    @Operation(
            summary = "Retrieve a user by email for administrators",
            description = "This endpoint retrieves a user by email for administrators."
    )
    public ResponseEntity<Optional<UserDTO>> getUserByEmailOnlyAdmin(@PathVariable String email, HttpServletRequest request) {
        return this.userServiceImpl.getUserByEmailOnlyAdmin(email, request);
    }

    /**
     * Retrieves a user by username for administrators.
     *
     * @param username The username of the user to retrieve.
     * @param request  The HttpServletRequest.
     * @return A ResponseEntity containing an Optional<UserDTO>.
     */
    @GetMapping("/{username}")
    @Operation(
            summary = "Retrieve a user by username for administrators",
            description = "This endpoint retrieves a user by username for administrators."
    )
    public ResponseEntity<Optional<UserDTO>> getUserByUsernameOnlyAdmin(@PathVariable String username, HttpServletRequest request) {
        return this.userServiceImpl.getUserByUsernameOnlyAdmin(username, request);
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @param request The HttpServletRequest.
     * @return An Optional<UserDTO> representing the currently authenticated user.
     */
    @GetMapping("/me")
    @Operation(
            summary = "Retrieve the currently authenticated user",
            description = "This endpoint retrieves the currently authenticated user."
    )
    public Optional<UserDTO> getMyUser(HttpServletRequest request) {
        return this.userServiceImpl.getMyUser(request);
    }
}
