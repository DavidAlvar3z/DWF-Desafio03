package com.letrasvivas.bookapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasvivas.bookapi.dto.request.CreateUserRequestDTO;
import com.letrasvivas.bookapi.dto.request.UpdateUserRequestDTO;
import com.letrasvivas.bookapi.entity.User;
import com.letrasvivas.bookapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disable security for tests
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // Helper method to create User entity
    private User createUser(String firstName, String lastName, String email, String phone, int age) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(passwordEncoder.encode("Password123!"))
                .phoneNumber(phone)
                .age(age)
                .role(User.Role.USER)
                .isActive(true)
                .build();
    }

    // Helper method to create DTO
    private CreateUserRequestDTO createUserDTO(String firstName, String lastName, String email, String phone, int age) {
        CreateUserRequestDTO dto = new CreateUserRequestDTO();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setPassword("Password123!");
        dto.setPhoneNumber(phone);
        dto.setAge(age);
        return dto;
    }

    @Test
    @DisplayName("Should create user successfully with valid data")
    void shouldCreateUserSuccessfully() throws Exception {
        CreateUserRequestDTO dto = createUserDTO("John", "Doe", "john.doe@example.com", "+1234567890", 25);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @DisplayName("Should return validation errors for invalid user data")
    void shouldReturnValidationErrorsForInvalidData() throws Exception {
        CreateUserRequestDTO dto = new CreateUserRequestDTO();
        dto.setFirstName("");
        dto.setLastName("D");
        dto.setEmail("invalid-email");
        dto.setPassword("123");
        dto.setPhoneNumber("123");
        dto.setAge(15);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should return conflict error when creating user with existing email")
    void shouldReturnConflictForDuplicateEmail() throws Exception {
        User existingUser = createUser("Jane", "Smith", "jane@example.com", "+1111111111", 28);
        userRepository.save(existingUser);

        CreateUserRequestDTO dto = createUserDTO("John", "Doe", "jane@example.com", "+2222222222", 25);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() throws Exception {
        User user = createUser("Alice", "Johnson", "alice@example.com", "+3333333333", 32);
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/v1/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Johnson"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.fullName").value("Alice Johnson"));
    }

    @Test
    @DisplayName("Should return 404 when user not found")
    void shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() throws Exception {
        User user = createUser("Bob", "Wilson", "bob@example.com", "+4444444444", 29);
        User savedUser = userRepository.save(user);

        UpdateUserRequestDTO updateDTO = new UpdateUserRequestDTO();
        updateDTO.setFirstName("Robert");
        updateDTO.setAge(30);

        mockMvc.perform(put("/api/v1/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Robert"))
                .andExpect(jsonPath("$.lastName").value("Wilson"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.fullName").value("Robert Wilson"));
    }

    @Test
    @DisplayName("Should get all users with pagination")
    void shouldGetAllUsersWithPagination() throws Exception {
        for (int i = 1; i <= 15; i++) {
            User user = createUser("User" + i, "Test", "user" + i + "@example.com", "+555000000" + i, 25 + i);
            userRepository.save(user);
        }

        mockMvc.perform(get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "firstName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(false));
    }

    @Test
    @DisplayName("Should search users by name")
    void shouldSearchUsersByName() throws Exception {
        userRepository.save(createUser("John", "Smith", "john.smith@example.com", "+6666666666", 30));
        userRepository.save(createUser("Jane", "Johnson", "jane.johnson@example.com", "+7777777777", 28));
        userRepository.save(createUser("Bob", "Brown", "bob.brown@example.com", "+8888888888", 35));

        mockMvc.perform(get("/api/v1/users/search")
                        .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("Should soft delete user successfully")
    void shouldSoftDeleteUserSuccessfully() throws Exception {
        User user = createUser("Delete", "Test", "delete@example.com", "+9999999999", 25);
        User savedUser = userRepository.save(user);

        mockMvc.perform(delete("/api/v1/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deactivated successfully"));

        mockMvc.perform(get("/api/v1/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(false));
    }
}
