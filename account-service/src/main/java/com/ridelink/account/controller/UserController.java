package com.ridelink.account.controller;

import com.ridelink.account.dto.ApiResponse;
import com.ridelink.account.dto.ChangePasswordRequest;
import com.ridelink.account.dto.UserResponse;
import com.ridelink.account.dto.UserUpdateRequest;
import com.ridelink.account.model.AccountStatus;
import com.ridelink.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Endpoints for user profiles and account lifecycle status")
public class UserController {

    private final AccountService accountService;

    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Admin endpoint to list all registered users.")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = accountService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok(users));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieves user profile details.")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String userId) {
        UserResponse user = accountService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile", description = "Updates user contact information and display name.")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserProfile(
            @PathVariable String userId,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse updated = accountService.updateUserProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", updated));
    }

    @PatchMapping("/{userId}/status")
    @Operation(summary = "Update account status", description = "Admin endpoint to activate, suspend, or deactivate an account.")
    public ResponseEntity<ApiResponse<UserResponse>> updateAccountStatus(
            @PathVariable String userId,
            @RequestParam AccountStatus status) {
        UserResponse updated = accountService.updateAccountStatus(userId, status);
        return ResponseEntity.ok(ApiResponse.ok("Account status updated to " + status, updated));
    }

    @PostMapping("/{userId}/change-password")
    @Operation(summary = "Change password", description = "Allows user to change their account password after verifying old password.")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable String userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        accountService.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Password changed successfully", null));
    }
}
