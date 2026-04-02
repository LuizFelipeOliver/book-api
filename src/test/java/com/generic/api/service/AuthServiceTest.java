package com.generic.api.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.generic.api.auth.AuthRequest;
import com.generic.api.auth.AuthResponse;
import com.generic.api.auth.AuthService;
import com.generic.api.auth.JwtService;
import com.generic.api.auth.User;
import com.generic.api.auth.UserRepository;
import com.generic.api.handler.ApiException;

/**
 * AuthServiceTest
 */
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_whenEmailNotExists_returnsToken() {
        // Arrange
        AuthRequest request = new AuthRequest("John", "john@email.com", "123456");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encoded123");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(any())).thenReturn("mocked-jwt-token");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertNotNull(response.token());
        assertEquals("mocked-jwt-token", response.token());
        verify(userRepository).save(any());
    }

    @Test
    void register_whenEmailAlreadyExists_throwsApiException() {
        // Arrange
        AuthRequest request = new AuthRequest("John", "john@email.com", "123456");
        User existingUser = new User("John", "john@email.com", "encoded123", "USER");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(existingUser));

        // Assert
        assertThrows(ApiException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_whenCredentialsAreValid_returnsToken() {
        // Arrange
        AuthRequest request = new AuthRequest("John", "john@email.com", "123456");
        User user = new User("John", "john@email.com", "encoded123", "USER");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("mocked-jwt-token");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response.token());
        assertEquals("mocked-jwt-token", response.token());
    }

    @Test
    void login_whenUserNotFound_throwsApiException() {
        // Arrange
        AuthRequest request = new AuthRequest("John", "john@email.com", "123456");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        // Assert
        ApiException ex = assertThrows(ApiException.class, () -> authService.login(request));
        assertEquals(404, ex.getStatus());
    }

    @Test
    void login_whenPasswordIsWrong_throwsApiException() {
        // Arrange
        AuthRequest request = new AuthRequest("John", "john@email.com", "wrong-password");
        User user = new User("John", "john@email.com", "encoded123", "USER");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        // Assert
        ApiException ex = assertThrows(ApiException.class, () -> authService.login(request));
        assertEquals(401, ex.getStatus());
    }
}
