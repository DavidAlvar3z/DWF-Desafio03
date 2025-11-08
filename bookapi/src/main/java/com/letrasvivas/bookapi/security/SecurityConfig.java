package com.letrasvivas.bookapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security Configuration with JWT Authentication
 *
 * Configures:
 * - JWT-based authentication
 * - CORS support for frontend applications
 * - Role-based access control (RBAC)
 * - Swagger/OpenAPI documentation access
 * - Stateless session management
 * - Public and protected endpoints
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.security.cors.allowed-origins}")
    private String[] allowedOrigins;

    /**
     * BCrypt Password Encoder Bean
     * Used for encoding passwords before storing in database
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DAO Authentication Provider Bean
     * Uses CustomUserDetailsService and PasswordEncoder for authentication
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication Manager Bean
     * Manages the authentication process
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Security Filter Chain Configuration
     *
     * Configures HTTP security for the application:
     * - CSRF protection disabled (for REST API)
     * - CORS enabled with custom configuration
     * - JWT-based authentication with custom filter
     * - Stateless session management
     * - Role-based access control (RBAC)
     * - Public endpoints: authentication, Swagger UI, health check
     * - Protected endpoints: require JWT token
     *
     * @param http HttpSecurity object to configure
     * @return Configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST API (token-based authentication is used instead)
                .csrf(csrf -> csrf.disable())

                // Enable CORS with custom configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configure exception handling for authentication failures
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                // Configure stateless session management (no session cookies)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure URL-based authorization
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC ENDPOINTS - Authentication (Login, Register)
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // PUBLIC ENDPOINTS - Create First User (For bootstrap)
                        // Allows creating the first admin user without authentication
                        // PUBLIC ENDPOINTS - Swagger/OpenAPI Documentation
                        // Allows access to API documentation without authentication
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/webjars/**"
                        ).permitAll()

                        // PUBLIC ENDPOINTS - Actuator Health Check
                        // Useful for load balancers and monitoring
                        .requestMatchers("/actuator/health").permitAll()

                        // PUBLIC ENDPOINTS - H2 Console (Development only)
                        // Remove in production!
                        .requestMatchers("/h2-console", "/h2-console/**").permitAll()

                        // PROTECTED ENDPOINTS - All other requests require authentication
                        // Client must provide valid JWT token in Authorization header
                        .anyRequest().authenticated()
                );

        // Allow H2 console frames (same origin) - Development only
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        // Set the authentication provider
        http.authenticationProvider(authenticationProvider());

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        // This filter validates JWT tokens for each request
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS Configuration Source Bean
     *
     * Configures CORS (Cross-Origin Resource Sharing) for frontend applications
     *
     * Allowed Origins (from application.properties):
     * - http://localhost:3000 (React default)
     * - http://localhost:4200 (Angular default)
     * - http://localhost:5173 (Vite default)
     *
     * Allowed Methods: GET, POST, PUT, DELETE, PATCH, OPTIONS
     * Allowed Headers: All (*)
     * Exposed Headers: Authorization (for JWT token)
     * Credentials: Allowed (cookies if needed)
     * Max Age: 3600 seconds (1 hour)
     *
     * @return CorsConfigurationSource configured for all paths
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Set allowed origins from application.properties
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));

        // Set allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Allow all headers (wildcard)
        configuration.setAllowedHeaders(List.of("*"));

        // Expose Authorization header so frontend can read JWT token
        configuration.setExposedHeaders(List.of("Authorization"));

        // Allow credentials (cookies, authorization headers, etc.)
        configuration.setAllowCredentials(true);

        // Set max age for preflight requests (1 hour)
        configuration.setMaxAge(3600L);

        // Register CORS configuration for all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
