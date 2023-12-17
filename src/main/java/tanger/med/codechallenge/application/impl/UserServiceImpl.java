package tanger.med.codechallenge.application.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tanger.med.codechallenge.api.dto.ImportSummaryDTO;
import tanger.med.codechallenge.api.dto.UserDTO;
import tanger.med.codechallenge.api.service.UserService;
import tanger.med.codechallenge.config.exception.FileIsEmptyException;
import tanger.med.codechallenge.config.exception.UserNotAdminException;
import tanger.med.codechallenge.config.exception.UserNotFoundException;
import tanger.med.codechallenge.domain.entity.User;
import tanger.med.codechallenge.domain.enums.Role;
import tanger.med.codechallenge.domain.mappers.UserMapper;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class responsible for user-related operations, including the generation of random user data.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Faker fakerConfig;
    private final UserRepo userRepo;
    private final AuthenticationServiceImpl authenticationService;
    private final JwtServiceImpl jwtServiceImpl;
    private final UserMapper userMapper;

    /**
     * Generates a list of random users with the specified count.
     *
     * @param count The number of users to generate.
     * @return A list of randomly generated User objects.
     */
    @Override
    public List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();

        for (long index = 0; index < count; index++) {
            User user = new User();
            user.setFirstName(this.fakerConfig.name().firstName());
            user.setLastName(this.fakerConfig.name().lastName());
            user.setBirthDate(this.fakerConfig.date().birthday());
            user.setCity(this.fakerConfig.address().city());
            user.setCountry(this.fakerConfig.address().countryCode());
            user.setAvatar(this.fakerConfig.internet().avatar());
            user.setCompany(this.fakerConfig.company().name());
            user.setJobPosition(this.fakerConfig.job().title());
            user.setMobile(this.fakerConfig.phoneNumber().cellPhone());
            user.setUsername(this.fakerConfig.name().username());
            user.setEmail(this.fakerConfig.internet().emailAddress());
            user.setPassword(this.fakerConfig.internet().password(6, 10)); // Random password between 6 and 10 characters
            user.setRole(this.fakerConfig.random().nextBoolean() ? Role.ADMIN : Role.USER);
            users.add(user);
        }
        return users;
    }

    /**
     * Downloads a JSON file containing randomly generated user data.
     *
     * @param count    The number of users to generate and include in the JSON file.
     * @param response The HttpServletResponse to set download headers.
     * @throws IOException If an I/O exception occurs during file writing.
     */
    @Override
    public void downloadUsersJson(int count, HttpServletResponse response) throws IOException {
        List<User> users = generateUsers(count);

        // Set headers for file download
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.json");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Use Jackson to convert users to a JSON string and write to the response output stream
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUsers = objectMapper.writeValueAsString(users.stream().map(userMapper::toDTO).collect(Collectors.toList()));
        response.getWriter().write(jsonUsers);
    }

    /**
     * Uploads a batch of users from a JSON file, checks for duplicates, encodes passwords, and saves to the database.
     *
     * @param file The MultipartFile representing the JSON file with user data.
     * @return A ResponseEntity containing the import summary.
     * @throws IOException If an I/O exception occurs during file processing.
     */
    @Override
    public ResponseEntity<ImportSummaryDTO> uploadUsersBatch(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users = objectMapper.readValue(file.getInputStream(), new TypeReference<List<User>>() {
        });
        if (users.isEmpty())
            throw new FileIsEmptyException("The uploaded file is empty");
        int totalRecords = users.size();
        int importedRecords = 0;

        for (User user : users) {
            // Check for duplicates based on email and username
            if (this.userRepo.findByEmail(user.getEmail()).isEmpty() && userRepo.findByUsername(user.getUsername()).isEmpty()) {
                authenticationService.register(userMapper.toDTO(user));
                importedRecords++;
            }
        }

        int failedImportRecords = totalRecords - importedRecords;

        ImportSummaryDTO dto = ImportSummaryDTO.builder()
                .importedRecords(importedRecords)
                .failedImportRecords(failedImportRecords)
                .totalRecords(totalRecords)
                .build();

        return ResponseEntity.ok(dto);
    }



    /**
     * Checks if the user associated with the provided HttpServletRequest is an admin.
     *
     * @param request The HttpServletRequest containing authorization data.
     * @return {@code true} if the user is an admin, {@code false} otherwise.
     * @throws UserNotFoundException If the user associated with the request is not found.
     */
    @Override
    public Boolean isAdmin(HttpServletRequest request) throws UserNotFoundException {
        // Retrieve user data based on authorization data from the request
        Optional<UserDTO> tmpUser = retrieveUserDataByAuthorizationData(request);

        // Throw UserNotFoundException if user data is not present
        tmpUser.orElseThrow(() -> new UserNotFoundException("User not found"));

        // Get the user's role
        Role role = tmpUser.get().getRole();

        // Check if the user is an admin
        return role.equals(Role.ADMIN);
    }


    /**
     * Retrieves a paginated list of all users.
     *
     * @param page The page number.
     * @param size The number of users per page.
     * @return A Page of UserDTOs.
     */
    @Override
    public ResponseEntity<Page<UserDTO>> getAllUsersOnlyAdmin(int page, int size, HttpServletRequest request) {
        if (isAdmin(request)) {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<UserDTO> userDTOSPage = this.userRepo.findAll(pageRequest).map(userMapper::toDTO);
            List<UserDTO> userDTOSContent = userDTOSPage.getContent();
            if (!userDTOSContent.isEmpty())
                return ResponseEntity.ok(userDTOSPage);
            else
                throw new UserNotFoundException("No user is found");


        }
        else {
            // User does not have 'ADMIN' role, return forbidden status
            throw new UserNotAdminException("The user should be an admin");
        }

    }

    /**
     * Retrieves a user by email only if the requester has 'ADMIN' role.
     *
     * @param email   The email of the user to retrieve.
     * @param request The HttpServletRequest for authentication.
     * @return A ResponseEntity containing an Optional<UserDTO>.
     */
    @Override
    public ResponseEntity<Optional<UserDTO>> getUserByEmailOnlyAdmin(String email, HttpServletRequest request) {
        if (isAdmin(request)) {
            Optional<UserDTO> userDto = this.userRepo.findByEmail(email).map(userMapper::toDTO);
            userDto.orElseThrow(() -> new UserNotFoundException("User not found"));

            return ResponseEntity.ok(userDto);
        } else {
            // User does not have 'ADMIN' role, return forbidden status
            throw new UserNotAdminException("The user should be an admin");
        }
    }


    /**
     * Retrieves a user by username only if the requester has 'ADMIN' role.
     *
     * @param email   The username of the user to retrieve.
     * @param request The HttpServletRequest for authentication.
     * @return A ResponseEntity containing an Optional<UserDTO>.
     */
    @Override
    public ResponseEntity<Optional<UserDTO>> getUserByUsernameOnlyAdmin(String email, HttpServletRequest request) {

        if (isAdmin(request)) {
            Optional<UserDTO> userDto = this.userRepo.findByUsername(email).map(userMapper::toDTO);
            userDto.orElseThrow(() -> new UserNotFoundException("User not found"));
            return ResponseEntity.ok(userDto);
        } else {
            // User does not have 'ADMIN' role, return forbidden status
            throw new UserNotAdminException("The user should be an admin");
        }
    }

    /**
     * Retrieves the user associated with the current request.
     *
     * @param request The HttpServletRequest for authentication.
     * @return An Optional<UserDTO> representing the current user.
     */
    @Override
    public ResponseEntity<Optional<UserDTO>> getMyUser(HttpServletRequest request) {
            Optional<UserDTO> userDTO = retrieveUserDataByAuthorizationData(request);
            // If userDTO is empty, throw UserNotFoundException
            userDTO.orElseThrow(() -> new UserNotFoundException("User not found"));
            return ResponseEntity.ok(userDTO);
    }


    /**
     * Retrieves user data based on the authorization data present in the HttpServletRequest.
     * This method assumes the use of JWT for authentication and extracts the user's email from the token.
     *
     * @param request The HttpServletRequest containing authorization data.
     * @return An Optional containing UserDTO if the user is found, or an empty Optional if not found.
     */
    @Override
    public Optional<UserDTO> retrieveUserDataByAuthorizationData(HttpServletRequest request) {
        // Retrieve the JWT from the Authorization header
        String authHeader = request.getHeader("Authorization");
        String jwt;
        String userEmail;

        // Extract the JWT token and user email from it
        jwt = authHeader.substring(7);
        userEmail = jwtServiceImpl.extractUsername(jwt);

        // Retrieve the user from the repository based on the extracted email
        Optional<User> user = this.userRepo.findByEmail(userEmail);

        user.orElseThrow(() -> new UserNotFoundException("User not found"));

        // Map the user entity to a UserDTO if the user is present
        return user.map(userMapper::toDTO);
    }


}
