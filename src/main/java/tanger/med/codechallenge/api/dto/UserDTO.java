package tanger.med.codechallenge.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tanger.med.codechallenge.domain.enums.Role;

import java.util.Date;

/**
 * Data Transfer Object (DTO) representing user-related information.
 * <p>
 * This class is used for transferring user data between different layers of the application.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object (DTO) representing user-related information.")
public class UserDTO {
    @Schema(description = "User's first name.", example = "John")
    private String firstName;

    @Schema(description = "User's last name.", example = "Doe")
    private String lastName;

    @Schema(description = "User's birth date.", example = "2000-12-16")
    private Date birthDate;

    @Schema(description = "User's city.", example = "New York")
    private String city;

    @Schema(description = "User's country.", example = "USA")
    private String country;

    @Schema(description = "URL to user's avatar image.", example = "http://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "User's company.", example = "ABC Inc.")
    private String company;

    @Schema(description = "User's job position.", example = "Developer")
    private String jobPosition;

    @Schema(description = "User's mobile number.", example = "+1 123-456-7890")
    private String mobile;

    @Schema(description = "User's username.", example = "johndoe")
    private String username;

    @Schema(description = "User's email address.", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's password.", example = "securepassword")
    private String password;

    @Schema(description = "User's role.", example = "ADMIN", allowableValues = {"ADMIN", "USER"})
    private Role role;
}