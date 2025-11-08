package com.letrasvivas.bookapi.config;

import com.letrasvivas.bookapi.entity.User;
import com.letrasvivas.bookapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Borrar TODOS los usuarios existentes
            userRepository.deleteAll();
            log.info("🗑️ Deleted all existing users");

            log.info("🔄 Initializing database with default users...");

            // Admin User
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("System")
                    .email("admin@letrasvivas.com")
                    .password(passwordEncoder.encode("Admin123!"))
                    .phoneNumber("+50312345678")
                    .age(30)
                    .role(User.Role.ADMIN)
                    .isActive(true)
                    .build();

            userRepository.save(admin);
            log.info("✅ Admin user created: {}", admin.getEmail());

            // Regular User
            User user = User.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("user@letrasvivas.com")
                    .password(passwordEncoder.encode("User123!"))
                    .phoneNumber("+50387654321")
                    .age(25)
                    .role(User.Role.USER)
                    .isActive(true)
                    .build();

            userRepository.save(user);
            log.info("✅ Regular user created: {}", user.getEmail());

            log.info("🎉 Database initialization completed!");
        };
    }
}
