package com.ridelink.account.controller;

import com.ridelink.account.dto.ApiResponse;
import com.ridelink.account.dto.AuthResponse;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.UserRegistrationRequest;
import com.ridelink.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication & Access", description = "Endpoints for user registration, login, and JWT token issuance")
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user account", description = "Registers passenger, driver, or admin account with hashed password and returns JWT token.")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
        AuthResponse response = accountService.register(request);
        return new ResponseEntity<>(ApiResponse.ok("User registered successfully", response), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user credentials and issues a JWT Bearer token.")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = accountService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Authentication successful", response));
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate JWT token", description = "Interservice validation endpoint to verify the authenticity of a token.")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> validateToken(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader;
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        boolean isValid = accountService.validateToken(token);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("valid", isValid)));
    }
}
