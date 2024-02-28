package com.startit.authservice.service;

import com.startit.authservice.exception.UserExistsException;
import com.startit.authservice.jwt.JwtService;
import com.startit.authservice.transfer.AuthResponse;
import com.startit.authservice.transfer.Credentials;
import com.startit.authservice.transfer.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthServiceTests {
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @Mock
    UserService userService;

    @InjectMocks
    AuthService authService;

    @Test
    public void testRegisterUser() {
        // Arrange
        Credentials credentials = new Credentials(new User("testUser", "password"), "password");
        AuthResponse expectedResponse = new AuthResponse(true, "accessToken", "refreshToken");
        when(userService.findByUsername("testUser")).thenReturn(Optional.empty());
        when(userService.save(credentials.getUser())).thenReturn(5L);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtService.generateToken("testUser")).thenReturn("accessToken");
        when(jwtService.generateRefreshToken("testUser")).thenReturn("refreshToken");

        // Act
        AuthResponse actualResponse = authService.register(credentials);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testLoginUser() {
        // Arrange
        Credentials credentials = new Credentials(new User("testUser", "password"), "password");
        AuthResponse expectedResponse = new AuthResponse("accessToken", "refreshToken");
        User existingUser = new User("testUser", "encodedPassword");
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken("testUser")).thenReturn("accessToken");
        when(jwtService.generateRefreshToken("testUser")).thenReturn("refreshToken");

        // Act
        AuthResponse actualResponse = authService.login(credentials);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testRegisterUser_UserExistsException() {
        // Arrange
        Credentials credentials = new Credentials(new User("existingUser", "password"), "password");
        when(userService.findByUsername("existingUser")).thenReturn(Optional.of(new User("existingUser", "encodedPassword")));

        // Act & Assert
        assertThrows(UserExistsException.class, () -> authService.register(credentials));
    }

    @Test
    public void testRegisterUser_UserNotSavedException() {
        // Arrange
        Credentials credentials = new Credentials(new User("existingUser", "password"), "password");
        when(userService.findByUsername("existingUser")).thenReturn(Optional.of(new User("existingUser", "encodedPassword")));
        when(userService.save(credentials.getUser())).thenReturn(0L);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.register(credentials));
    }
}
