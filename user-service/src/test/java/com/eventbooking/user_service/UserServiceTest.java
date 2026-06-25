package com.eventbooking.user_service;

import com.eventbooking.user_service.dto.LoginResponse;
import com.eventbooking.user_service.dto.RegisterRequest;
import com.eventbooking.user_service.dto.UserResponse;
import com.eventbooking.user_service.model.Role;
import com.eventbooking.user_service.model.User;
import com.eventbooking.user_service.repository.UserRepository;
import com.eventbooking.user_service.config.JwtUtil;
import com.eventbooking.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// WHY: Tells JUnit to use Mockito extension
// Enables @Mock and @InjectMocks annotations
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // WHY: Creates fake UserRepository
    // No real MongoDB needed
    @Mock
    private UserRepository userRepository;

    // WHY: Creates fake JwtUtil
    // No real JWT generation needed
    @Mock
    private JwtUtil jwtUtil;

    // WHY: Creates real UserService
    // Injects mocks automatically
    @InjectMocks
    private UserService userService;

    // WHY: Test data we reuse across tests
    private User testUser;
    private RegisterRequest registerRequest;

    // WHY: Runs before EACH test
    // Sets up fresh test data
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("usr001");
        testUser.setName("John Doe");
        testUser.setEmail("john@gmail.com");
        testUser.setPassword("john123");
        testUser.setRole(Role.CUSTOMER);

        registerRequest = new RegisterRequest();
        registerRequest.setName("John Doe");
        registerRequest.setEmail("john@gmail.com");
        registerRequest.setPassword("john123");
        registerRequest.setRole(Role.CUSTOMER);
    }

    // ===== REGISTER TESTS =====

    // TEST 1: Successful registration
    @Test
    void register_Success() {

        // WHY: Tell fake repository what to return
        // existsByEmail returns false = email not taken
        when(userRepository.findByEmail(
                "john@gmail.com"))
                .thenReturn(Optional.empty());

        // WHY: Tell fake repository to return
        // our test user when save is called
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        // WHY: Call actual method we are testing
        UserResponse response = userService
                .register(registerRequest);

        // WHY: Verify response is correct
        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@gmail.com", response.getEmail());
        assertEquals(Role.CUSTOMER, response.getRole());

        // WHY: Verify save was called exactly once
        verify(userRepository, times(1))
                .save(any(User.class));
    }

    // TEST 2: Register with duplicate email
    @Test
    void register_DuplicateEmail_ThrowsException() {

        // WHY: Tell fake repository email already exists
        when(userRepository.findByEmail(
                "john@gmail.com"))
                .thenReturn(Optional.of(testUser));

        // WHY: Verify exception is thrown
        // with correct message
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.register(registerRequest)
        );

        assertEquals(
                "Emailid already registered",
                exception.getMessage()
        );

        // WHY: Verify save was NEVER called
        // no user should be saved on duplicate email
        verify(userRepository, never())
                .save(any(User.class));
    }

    // ===== LOGIN TESTS =====

    // TEST 3: Successful login
    @Test
    void login_Success() {

        // WHY: Tell fake repository to return user
        when(userRepository.findByEmail(
                "john@gmail.com"))
                .thenReturn(Optional.of(testUser));

        // WHY: Tell fake JwtUtil to return token
        when(jwtUtil.generateToken(
                anyString(), anyString(), anyString()))
                .thenReturn("fake-jwt-token");

        // WHY: Call login method
        LoginResponse response = userService.login(
                "john@gmail.com", "john123");

        // WHY: Verify response is correct
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("john@gmail.com", response.getEmail());
        assertEquals("CUSTOMER", response.getRole());
    }

    // TEST 4: Login with wrong password
    @Test
    void login_WrongPassword_ThrowsException() {

        // WHY: User exists but password is wrong
        when(userRepository.findByEmail(
                "john@gmail.com"))
                .thenReturn(Optional.of(testUser));

        // WHY: Verify exception thrown
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.login(
                        "john@gmail.com", "wrongpassword")
        );

        assertEquals(
                "Invalid Password",
                exception.getMessage()
        );
    }

    // TEST 5: Login with non existing email
    @Test
    void login_UserNotFound_ThrowsException() {

        // WHY: No user found with this email
        when(userRepository.findByEmail(
                "nobody@gmail.com"))
                .thenReturn(Optional.empty());

        // WHY: Verify exception thrown
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.login(
                        "nobody@gmail.com", "password")
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );
    }

    // TEST 6: Get user by valid ID
    @Test
    void getUserById_Success() {

        // WHY: Tell fake repository to return user
        when(userRepository.findById("usr001"))
                .thenReturn(Optional.of(testUser));

        // WHY: Call method
        UserResponse response = userService
                .getUserById("usr001");

        // WHY: Verify correct user returned
        assertNotNull(response);
        assertEquals("usr001", response.getId());
        assertEquals("John Doe", response.getName());
    }

    // TEST 7: Get user by invalid ID
    @Test
    void getUserById_NotFound_ThrowsException() {

        // WHY: No user found with this ID
        when(userRepository.findById("invalid"))
                .thenReturn(Optional.empty());

        // WHY: Verify exception thrown
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getUserById("invalid")
        );

        assertEquals(
                "User Not Found",
                exception.getMessage()
        );
    }
}