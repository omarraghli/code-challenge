package tanger.med.codechallenge.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tanger.med.codechallenge.api.dto.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dto.AuthenticationResponseDTO;
import tanger.med.codechallenge.application.impl.AuthenticationServiceImpl;

/**
 * Controller class for handling authentication-related requests.
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationServiceImpl service;

    /**
     * Handles authentication requests and returns the authentication response.
     *
     * @param request The authentication request DTO containing user credentials.
     * @return ResponseEntity containing the authentication response DTO.
     */

    @Operation(summary = "Authenticate User", description = "Generate JWT Token based on username/email and password.")
    @PostMapping("/api/auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponseDTO.class)),
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(schema = @Schema(implementation = BadRequestException.class))
            })
    })
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request
    ) {
        return service.authenticate(request);
    }

}