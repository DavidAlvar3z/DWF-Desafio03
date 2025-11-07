package com.letrasvivas.bookapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    @Builder.Default
    private String type = "Bearer";

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Long expiresIn;
}
