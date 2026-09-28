package com.ridelink.account.service;

import com.ridelink.account.dto.AuthResponse;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.UserRegistrationRequest;
import com.ridelink.account.exception.DuplicateResourceException;
import com.ridelink.account.exception.InvalidCredentialsException;
import com.ridelink.account.exception.UserSuspendedException;
import com.ridelink.account.model.AccountStatus;
import com.ridelink.account.model.Role;
import com.ridelink.account.model.User;
import com.ridelink.account.repository.UserRepository;
import com.ridelink.account.security.JwtTokenProvider;
import com.ridelink.account.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(
                "passenger@example.com",
                "encodedPassword123",
                "Alice Smith",
                "+94771112233",
                Role.ROLE_PASSENGER
        );
        sampleUser.setId("user-001");
        sampleUser.setStatus(AccountStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void testRegister_Success() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "newuser@example.com", "secret123", "Bob Jones", "+94772223344", Role.ROLE_PASSENGER
        );

        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encodedSecret");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId("user-002");
            return u;
        });
        when(jwtTokenProvider.generateToken(any(), any(), any())).thenReturn("mock.jwt.token");
        when(jwtTokenProvider.getExpirationMs()).thenReturn(86400000L);

        AuthResponse response = accountService.register(request);

        assertNotNull(response);
        assertEquals("user-002", response.getUserId());
        assertEquals("newuser@example.com", response.getEmail());
        assertEquals("mock.jwt.token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Negative Scenario: Should throw DuplicateResourceException when email is already registered")
    void testRegister_DuplicateEmail_ThrowsException() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "passenger@example.com", "secret123", "Alice Smith", "+94771112233", Role.ROLE_PASSENGER
        );

        when(userRepository.existsByEmail("passenger@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> accountService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void testLogin_Success() {
        LoginRequest request = new LoginRequest("passenger@example.com", "password123");

        when(userRepository.findByEmail("passenger@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("password123", "encodedPassword123")).thenReturn(true);
        when(jwtTokenProvider.generateToken("passenger@example.com", "ROLE_PASSENGER", "user-001")).thenReturn("valid.jwt.token");
        when(jwtTokenProvider.getExpirationMs()).thenReturn(86400000L);

        AuthResponse response = accountService.login(request);

        assertNotNull(response);
        assertEquals("valid.jwt.token", response.getToken());
        assertEquals("user-001", response.getUserId());
    }

    @Test
    @DisplayName("Negative Scenario: Should throw InvalidCredentialsException for wrong password")
    void testLogin_WrongPassword_ThrowsException() {
        LoginRequest request = new LoginRequest("passenger@example.com", "wrongPassword");

        when(userRepository.findByEmail("passenger@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword123")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> accountService.login(request));
    }

    @Test
    @DisplayName("Negative Scenario: Should throw UserSuspendedException for suspended user")
    void testLogin_SuspendedAccount_ThrowsException() {
        sampleUser.setStatus(AccountStatus.SUSPENDED);
        LoginRequest request = new LoginRequest("passenger@example.com", "password123");

        when(userRepository.findByEmail("passenger@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("password123", "encodedPassword123")).thenReturn(true);

        assertThrows(UserSuspendedException.class, () -> accountService.login(request));
    }
}
