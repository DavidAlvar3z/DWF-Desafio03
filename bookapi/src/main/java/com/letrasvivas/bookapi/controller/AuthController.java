package com.letrasvivas.bookapi.controller;

import com.letrasvivas.bookapi.dto.request.CreateUserRequestDTO;
import com.letrasvivas.bookapi.dto.request.LoginRequestDTO;
import com.letrasvivas.bookapi.dto.response.LoginResponseDTO;
import com.letrasvivas.bookapi.dto.response.UserResponseDTO;
import com.letrasvivas.bookapi.entity.User;
import com.letrasvivas.bookapi.repository.UserRepository;
import com.letrasvivas.bookapi.security.JwtUtil;
import com.letrasvivas.bookapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Login endpoint
     * Public - No authentication required
     */
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtUtil.generateToken(authentication);

            // Get user details
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Build response
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .token(jwt)
                    .type("Bearer")
                    .userId(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().name())
                    .expiresIn(jwtUtil.getExpirationMs())
                    .build();

            log.info("User logged in successfully: {}", user.getEmail());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            log.error("Login failed for user: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        } catch (Exception e) {
            log.error("Login error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }

    /**
     * Register endpoint
     * Public - No authentication required
     */
    @Operation(
            summary = "Register new user",
            description = "Public endpoint for user registration. Allows new users to create an account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody CreateUserRequestDTO createUserDTO) {
        log.info("📝 New user registration attempt: {}", createUserDTO.getEmail());
        UserResponseDTO createdUser = userService.createUser(createUserDTO);
        log.info("✅ User registered successfully: {}", createdUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Validate token endpoint
     */
    @Operation(summary = "Validate token", description = "Check if JWT token is valid")
    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            boolean isValid = jwtUtil.validateToken(jwt);
            if (isValid) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                return ResponseEntity.ok().body(
                        new ValidationResponse(true, "Token is valid", username)
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ValidationResponse(false, "Token is invalid", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ValidationResponse(false, "Token validation failed", null));
        }
    }

    // Inner class for validation response
    private record ValidationResponse(boolean valid, String message, String username) {}
}