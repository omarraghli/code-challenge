package tanger.med.codechallenge.application.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tanger.med.codechallenge.api.dtos.AuthenticationRequestDTO;
import tanger.med.codechallenge.api.dtos.AuthenticationResponseDTO;
import tanger.med.codechallenge.application.services.AuthenticationServiceImpl;

import java.io.IOException;

@RestController
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationServiceImpl service;

    @PostMapping("/api/auth")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}