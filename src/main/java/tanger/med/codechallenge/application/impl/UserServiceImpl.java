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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tanger.med.codechallenge.api.dto.ImportSummaryDTO;
import tanger.med.codechallenge.api.dto.UserDTO;
import tanger.med.codechallenge.api.service.UserService;
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

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Retrieves a paginated list of all users.
     *
     * @param page The page number.
     * @param size The number of users per page.
     * @return A Page of UserDTOs.
     */
    @Override
    public Page<UserDTO> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.userRepo.findAll(pageRequest).map(userMapper::toDTO);
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
        Optional<UserDTO> tmpUser = getMyUser(request);
        Role role = Role.USER;

        if (tmpUser.isPresent()) {
            role = tmpUser.get().getRole();
        }

        if (role.equals(Role.ADMIN)) {
            Optional<UserDTO> userDto = this.userRepo.findByEmail(email).map(userMapper::toDTO);
            return ResponseEntity.ok(userDto);
        } else {
            // User does not have 'ADMIN' role, return forbidden status
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Optional.empty());
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
        Optional<UserDTO> tmpUser = getMyUser(request);
        Role role = Role.USER;

        if (tmpUser.isPresent()) {
            role = tmpUser.get().getRole();
        }

        if (role.equals(Role.ADMIN)) {
            Optional<UserDTO> userDto = this.userRepo.findByUsername(email).map(userMapper::toDTO);
            return ResponseEntity.ok(userDto);
        } else {
            // User does not have 'ADMIN' role, return forbidden status
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Optional.empty());
        }
    }

    /**
     * Retrieves the user associated with the current request.
     *
     * @param request The HttpServletRequest for authentication.
     * @return An Optional<UserDTO> representing the current user.
     */
    @Override
    public Optional<UserDTO> getMyUser(HttpServletRequest request) {
        //here we are sure to have the email and not the username
        String authHeader = request.getHeader("Authorization");
        String jwt;
        String userEmail;

        jwt = authHeader.substring(7);
        userEmail = jwtServiceImpl.extractUsername(jwt);

        Optional<User> user = this.userRepo.findByEmail(userEmail);


        return user.map(userMapper::toDTO);
    }

}
