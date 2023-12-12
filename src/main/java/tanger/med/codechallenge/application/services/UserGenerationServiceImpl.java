package tanger.med.codechallenge.application.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tanger.med.codechallenge.api.dtos.ImportSummaryDTO;
import tanger.med.codechallenge.api.interfaces.UserGenerationService;
import tanger.med.codechallenge.config.ApplicationConfiguration;
import tanger.med.codechallenge.domain.entities.User;
import tanger.med.codechallenge.domain.enums.Role;
import tanger.med.codechallenge.domain.repositories.UserRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for user-related operations, including the generation of random user data.
 */
@Service
@RequiredArgsConstructor
public class UserGenerationServiceImpl implements UserGenerationService {

    private final Faker fakerConfig;
    private final UserRepo userRepo;
    private final ApplicationConfiguration applicationConfiguration;




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
        String jsonUsers = objectMapper.writeValueAsString(users);
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
        List<User> users = objectMapper.readValue(file.getInputStream(), new TypeReference<List<User>>() {});

        int totalRecords = users.size();
        int importedRecords = 0;

        for (User user : users) {
            // Check for duplicates based on email and username
            if (this.userRepo.findByEmail(user.getEmail()) == null && userRepo.findByUsername(user.getUsername()) == null) {
                // Encode password before saving
                user.setPassword(applicationConfiguration.passwordEncoder().encode(user.getPassword()));
                userRepo.save(user);
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
}
