package com.letrasvivas.bookapi.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer age;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String fullName;
    private Integer subscriptionCount;
    private String role;

    private List<UserSubscriptionSummaryDTO> activeSubscriptions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSubscriptionSummaryDTO {
        private Long id;
        private String planName;
        private String status;
        private String endDate;
    }
}
