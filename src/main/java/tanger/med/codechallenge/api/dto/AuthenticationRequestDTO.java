package tanger.med.codechallenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an authentication request.
 * <p>
 * This class is used for transferring authentication request data between different layers of the application.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {

    /**
     * The email associated with the authentication request.
     */
    private String email;

    /**
     * The password associated with the authentication request.
     */
    private String password;
}
