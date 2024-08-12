package com.Server.saleWebsite.auth;


import com.Server.saleWebsite.config.LogoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details")
    @ApiResponse(responseCode = "200", description = "Successful registration", content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterWrapper requestWrapper
    ) {
        RegisterRequest request = requestWrapper.getData();
        return ResponseEntity.ok(authenticationService.register(request));
    }


    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns a token")
    @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Refreshes the authentication token")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logs out the current user")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    public void logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal Authentication authentication) {
        logoutService.logout(request, response, authentication);
    }
}