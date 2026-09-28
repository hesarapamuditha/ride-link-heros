package com.ridelink.account.service;

import com.ridelink.account.dto.AuthResponse;
import com.ridelink.account.dto.ChangePasswordRequest;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.UserRegistrationRequest;
import com.ridelink.account.dto.UserResponse;
import com.ridelink.account.dto.UserUpdateRequest;
import com.ridelink.account.model.AccountStatus;

import java.util.List;

public interface AccountService {

    AuthResponse register(UserRegistrationRequest request);

    AuthResponse login(LoginRequest request);

    boolean validateToken(String token);

    UserResponse getUserById(String userId);

    UserResponse getUserByEmail(String email);

    UserResponse updateUserProfile(String userId, UserUpdateRequest request);

    UserResponse updateAccountStatus(String userId, AccountStatus status);

    void changePassword(String userId, ChangePasswordRequest request);

    List<UserResponse> getAllUsers();
}
