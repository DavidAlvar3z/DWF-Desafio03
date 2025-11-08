package com.letrasvivas.bookapi.controller;

import com.letrasvivas.bookapi.dto.request.CreateUserRequestDTO;
import com.letrasvivas.bookapi.dto.request.UpdateUserRequestDTO;
import com.letrasvivas.bookapi.dto.response.UserResponseDTO;
import com.letrasvivas.bookapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/users")  // ✅ CAMBIO AQUÍ: Agregado /users
@Tag(name = "User Management", description = "APIs for managing users in the system - Requires JWT Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users with pagination
     * Only accessible to ADMIN role
     */
    @Operation(
            summary = "Get all users with pagination",
            description = "Retrieve a paginated list of all users in the system. Supports sorting by multiple fields. [ADMIN ONLY]"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "100") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserResponseDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by ID
     * Users can only view their own profile, Admins can view any profile
     */
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a specific user by their unique identifier. Users can view their own profile, Admins can view any profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Cannot view other user's profile"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")  // GET /api/v1/admin/users/{id}
    @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#id)")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Create a new user
     * Only ADMIN can create users through this endpoint
     * For public registration, use /api/v1/auth/register
     */
    @Operation(
            summary = "Create a new user",
            description = "Create a new user in the system with the provided information. [ADMIN ONLY]"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping  // POST /api/v1/admin/users
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(
            @Parameter(description = "User data to create", required = true)
            @Valid @RequestBody CreateUserRequestDTO createUserDTO
    ) {
        UserResponseDTO createdUser = userService.createUser(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Update an existing user
     * Users can only update their own profile, Admins can update any profile
     */
    @Operation(
            summary = "Update an existing user",
            description = "Update user information. Only provided fields will be updated. Users can update their own profile, Admins can update any profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Cannot update other user's profile"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")  // PUT /api/v1/admin/users/{id}
    @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#id)")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated user data", required = true)
            @Valid @RequestBody UpdateUserRequestDTO updateUserDTO
    ) {
        UserResponseDTO updatedUser = userService.updateUser(id, updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Soft delete a user (deactivate)
     * Users can only deactivate their own account, Admins can deactivate any account
     */
    @Operation(
            summary = "Soft delete a user",
            description = "Deactivate a user by setting isActive to false. User data is preserved. Users can deactivate their own account, Admins can deactivate any account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Cannot delete other user's account"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")  // DELETE /api/v1/admin/users/{id}
    @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#id)")
    public ResponseEntity<Map<String, String>> deleteUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deactivated successfully");
        response.put("userId", id.toString());
        return ResponseEntity.ok(response);
    }

    /**
     * Permanently delete a user
     * Only ADMIN can permanently delete users
     */
    @Operation(
            summary = "Permanently delete a user",
            description = "Permanently remove a user from the system. This action cannot be undone. [ADMIN ONLY]"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User permanently deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}/permanent")  // DELETE /api/v1/admin/users/{id}/permanent
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> permanentlyDeleteUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        userService.permanentlyDeleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User permanently deleted");
        response.put("userId", id.toString());
        return ResponseEntity.ok(response);
    }

    /**
     * Search users by name
     * Available to authenticated users
     */
    @Operation(
            summary = "Search users by name",
            description = "Search users by first name or last name (case-insensitive partial match)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")  // GET /api/v1/admin/users/search
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByName(
            @Parameter(description = "Name to search for", required = true, example = "John")
            @RequestParam String name
    ) {
        List<UserResponseDTO> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Get active users
     * Only ADMIN can view all active users
     */
    @Operation(
            summary = "Get active users",
            description = "Retrieve all users that are currently active in the system. [ADMIN ONLY]"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")  // GET /api/v1/admin/users/active
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {
        List<UserResponseDTO> activeUsers = userService.getActiveUsers();
        return ResponseEntity.ok(activeUsers);
    }

    /**
     * Get users by age range
     * Available to authenticated users
     */
    @Operation(
            summary = "Get users by age range",
            description = "Retrieve users within a specific age range"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid age parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/age-range")  // GET /api/v1/admin/users/age-range
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByAgeRange(
            @Parameter(description = "Minimum age", required = true, example = "18")
            @RequestParam @Min(16) @Max(120) Integer minAge,
            @Parameter(description = "Maximum age", required = true, example = "65")
            @RequestParam @Min(16) @Max(120) Integer maxAge
    ) {
        List<UserResponseDTO> users = userService.getUsersByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users with active subscriptions
     * Available to authenticated users
     */
    @Operation(
            summary = "Get users with active subscriptions",
            description = "Retrieve all users who currently have at least one active subscription"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users with active subscriptions retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/with-subscriptions")  // GET /api/v1/admin/users/with-subscriptions
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersWithActiveSubscriptions() {
        List<UserResponseDTO> users = userService.getUsersWithActiveSubscriptions();
        return ResponseEntity.ok(users);
    }

    /**
     * Advanced user search with multiple criteria
     * Only ADMIN can use advanced search
     */
    @Operation(
            summary = "Advanced user search",
            description = "Search users using multiple criteria with pagination support. [ADMIN ONLY]"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/advanced-search")  // GET /api/v1/admin/users/advanced-search
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> advancedSearch(
            @Parameter(description = "First name filter", example = "John")
            @RequestParam(required = false) String firstName,
            @Parameter(description = "Last name filter", example = "Doe")
            @RequestParam(required = false) String lastName,
            @Parameter(description = "Email filter", example = "john@example.com")
            @RequestParam(required = false) String email,
            @Parameter(description = "Active status filter", example = "true")
            @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Minimum age", example = "18")
            @RequestParam(required = false) @Min(16) Integer minAge,
            @Parameter(description = "Maximum age", example = "65")
            @RequestParam(required = false) @Max(120) Integer maxAge,
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort criteria", example = "firstName,asc")
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Pageable pageable = createPageable(page, size, sort);
        Page<UserResponseDTO> users = userService.searchUsers(
                firstName, lastName, email, isActive, minAge, maxAge, pageable
        );
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by email
     * Users can only view their own email, Admins can view any email
     */
    @Operation(
            summary = "Get user by email",
            description = "Retrieve a user by their email address"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/email/{email}")  // GET /api/v1/admin/users/email/{email}
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserByEmail(
            @Parameter(description = "User email", required = true, example = "john@example.com")
            @PathVariable String email
    ) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Check if email exists
     * Available to unauthenticated users (for registration validation)
     */
    @Operation(
            summary = "Check if email exists",
            description = "Check whether an email address is already registered in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email check completed",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"exists\": true, \"email\": \"john@example.com\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/email/{email}/exists")  // GET /api/v1/admin/users/email/{email}/exists
    @PreAuthorize("permitAll()")
    public ResponseEntity<Map<String, Object>> checkEmailExists(
            @Parameter(description = "Email to check", required = true, example = "john@example.com")
            @PathVariable String email
    ) {
        boolean exists = userService.emailExists(email);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("email", email);
        return ResponseEntity.ok(response);
    }

    /**
     * Get user statistics
     * Only ADMIN can view statistics
     */
    @Operation(
            summary = "Get user statistics",
            description = "Get general statistics about users in the system. [ADMIN ONLY]"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"totalUsers\": 100, \"activeUsers\": 85}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/statistics")  // GET /api/v1/admin/users/statistics
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getUserCount());
        stats.put("activeUsers", userService.getActiveUserCount());
        return ResponseEntity.ok(stats);
    }

    /**
     * Helper method to create Pageable object from sort parameters
     */
    private Pageable createPageable(int page, int size, String[] sort) {
        try {
            List<Sort.Order> orders = new ArrayList<>();

            // Si el array está vacío o es null, usar ordenamiento por defecto
            if (sort == null || sort.length == 0) {
                return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
            }

            for (String sortParam : sort) {
                // Eliminar espacios en blanco
                sortParam = sortParam.trim();

                // Si el parámetro está vacío, continuar
                if (sortParam.isEmpty()) {
                    continue;
                }

                // Split por coma: "field,direction"
                String[] parts = sortParam.split(",");

                if (parts.length == 0) {
                    continue;
                }

                // Obtener el nombre de la propiedad
                String property = parts[0].trim();

                // Validar que la propiedad no esté vacía
                if (property.isEmpty()) {
                    continue;
                }

                // Default direction es ASC
                Sort.Direction direction = Sort.Direction.ASC;

                // Si hay una segunda parte, parsear la dirección
                if (parts.length > 1) {
                    String directionStr = parts[1].trim().toUpperCase();
                    if (directionStr.equals("DESC")) {
                        direction = Sort.Direction.DESC;
                    }
                    // Si es "ASC" o cualquier otra cosa, mantener ASC por defecto
                }

                // Agregar la orden a la lista
                orders.add(new Sort.Order(direction, property));
            }

            // Si no se agregó ninguna orden, usar ordenamiento por defecto
            if (orders.isEmpty()) {
                return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
            }

            return PageRequest.of(page, size, Sort.by(orders));

        } catch (Exception e) {
            log.error("❌ Error creating Pageable: {}", e.getMessage(), e);
            // Return default pagination if there's an error
            return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        }
    }
}
