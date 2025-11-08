package com.letrasvivas.bookapi;

import com.letrasvivas.bookapi.dto.request.CreateUserRequestDTO;
import com.letrasvivas.bookapi.dto.request.UpdateUserRequestDTO;
import com.letrasvivas.bookapi.dto.response.UserResponseDTO;
import com.letrasvivas.bookapi.entity.User;
import com.letrasvivas.bookapi.exception.DuplicateResourceException;
import com.letrasvivas.bookapi.exception.ResourceNotFoundException;
import com.letrasvivas.bookapi.repository.UserRepository;
import com.letrasvivas.bookapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;  // ← AGREGADO

    @InjectMocks
    private UserService userService;

    private User testUser;
    private CreateUserRequestDTO createUserRequestDTO;
    private UpdateUserRequestDTO updateUserRequestDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("encodedPassword");  // ← AGREGADO
        testUser.setPhoneNumber("+1234567890");
        testUser.setAge(25);
        testUser.setRole(User.Role.USER);  // ← AGREGADO
        testUser.setIsActive(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        createUserRequestDTO = new CreateUserRequestDTO();
        createUserRequestDTO.setFirstName("Jane");
        createUserRequestDTO.setLastName("Smith");
        createUserRequestDTO.setEmail("jane.smith@example.com");
        createUserRequestDTO.setPassword("Password123!");  // ← AGREGADO
        createUserRequestDTO.setPhoneNumber("+1987654321");
        createUserRequestDTO.setAge(30);

        updateUserRequestDTO = new UpdateUserRequestDTO();
        updateUserRequestDTO.setFirstName("John Updated");
        updateUserRequestDTO.setAge(26);
    }

    @Test
    void getAllUsers_ShouldReturnPageOfUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponseDTO> result = userService.getAllUsers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("john.doe@example.com", result.getContent().get(0).getEmail());
        verify(userRepository).findAll(pageable);
    }

    @Test
    void getUserById_WithValidId_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
        verify(userRepository).findById(999L);
    }

    @Test
    void createUser_WithValidData_ShouldCreateUser() {
        when(userRepository.existsByEmail(createUserRequestDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");  // ← AGREGADO
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponseDTO result = userService.createUser(createUserRequestDTO);

        assertNotNull(result);
        verify(userRepository).existsByEmail(createUserRequestDTO.getEmail());
        verify(passwordEncoder).encode("Password123!");  // ← AGREGADO
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldThrowDuplicateResourceException() {
        when(userRepository.existsByEmail(createUserRequestDTO.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(createUserRequestDTO);
        });
        verify(userRepository).existsByEmail(createUserRequestDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WithValidData_ShouldUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponseDTO result = userService.updateUser(1L, updateUserRequestDTO);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(999L, updateUserRequestDTO);
        });
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WithValidId_ShouldSoftDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        assertFalse(testUser.getIsActive());
    }

    @Test
    void searchUsersByName_ShouldReturnMatchingUsers() {
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(users);

        List<UserResponseDTO> result = userService.searchUsersByName("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(userRepository).findByNameContainingIgnoreCase("John");
    }

    @Test
    void getActiveUsers_ShouldReturnOnlyActiveUsers() {
        List<User> activeUsers = Arrays.asList(testUser);
        when(userRepository.findByIsActiveTrue()).thenReturn(activeUsers);

        List<UserResponseDTO> result = userService.getActiveUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsActive());
        verify(userRepository).findByIsActiveTrue();
    }

    @Test
    void emailExists_WithExistingEmail_ShouldReturnTrue() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        boolean result = userService.emailExists("john.doe@example.com");

        assertTrue(result);
        verify(userRepository).existsByEmail("john.doe@example.com");
    }

    @Test
    void emailExists_WithNonExistingEmail_ShouldReturnFalse() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        boolean result = userService.emailExists("nonexistent@example.com");

        assertFalse(result);
        verify(userRepository).existsByEmail("nonexistent@example.com");
    }
}
