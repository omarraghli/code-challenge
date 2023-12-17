package tanger.med.codechallenge.api.dto;

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
public class UserDTO {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String city;
    private String country;
    private String avatar;
    private String company;
    private String jobPosition;
    private String mobile;
    private String username;
    private String email;
    private String password;
    private Role role;
}
