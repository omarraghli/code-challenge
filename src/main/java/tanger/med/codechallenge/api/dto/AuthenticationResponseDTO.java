package tanger.med.codechallenge.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an authentication response.
 * <p>
 * This class is used for transferring authentication response data between different layers of the application.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    // The access token received as part of the authentication response
    @JsonProperty("accessToken")
    private String accessToken;
}
