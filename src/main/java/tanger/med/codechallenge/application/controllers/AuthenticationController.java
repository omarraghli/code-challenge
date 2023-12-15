package tanger.med.codechallenge.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tanger.med.codechallenge.api.dtos.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dtos.AuthenticationResponseDTO;
import tanger.med.codechallenge.application.services.AuthenticationServiceImpl;

import java.io.IOException;

/**
 * Controller class for handling authentication-related requests.
 */
@RestController
@RequiredArgsConstructor
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
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
