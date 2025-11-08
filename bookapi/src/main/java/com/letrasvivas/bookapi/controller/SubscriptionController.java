package com.letrasvivas.bookapi.controller;

import com.letrasvivas.bookapi.dto.request.CreateSubscriptionRequestDTO;
import com.letrasvivas.bookapi.dto.request.UpdateSubscriptionRequestDTO;
import com.letrasvivas.bookapi.dto.response.SubscriptionResponseDTO;
import com.letrasvivas.bookapi.entity.Subscription.SubscriptionStatus;
import com.letrasvivas.bookapi.entity.User;
import com.letrasvivas.bookapi.exception.ResourceNotFoundException;
import com.letrasvivas.bookapi.repository.UserRepository;
import com.letrasvivas.bookapi.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Subscription Management", description = "APIs for managing user subscriptions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    @Autowired
    public SubscriptionController(
            SubscriptionService subscriptionService,
            UserRepository userRepository
    ) {
        this.subscriptionService = subscriptionService;
        this.userRepository = userRepository;
    }

    /**
     * Get current authenticated user's subscriptions
     * Endpoint: GET /api/v1/subscriptions/my-subscriptions
     */
    @Operation(
            summary = "Get my subscriptions",
            description = "Get all subscriptions for the current authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/my-subscriptions")
    public ResponseEntity<List<SubscriptionResponseDTO>> getMySubscriptions(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Get user from database
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getSubscriptionsByUserId(user.getId());

        return ResponseEntity.ok(subscriptions);
    }

    @Operation(
            summary = "Get all subscriptions with pagination",
            description = "Retrieve a paginated list of all subscriptions in the system with sorting support"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<Page<SubscriptionResponseDTO>> getAllSubscriptions(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort criteria", example = "createdAt,desc")
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Pageable pageable = createPageable(page, size, sort);
        Page<SubscriptionResponseDTO> subscriptions = subscriptionService.getAllSubscriptions(pageable);
        return ResponseEntity.ok(subscriptions);
    }

    @Operation(
            summary = "Get subscription by ID",
            description = "Retrieve a specific subscription by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> getSubscriptionById(
            @Parameter(description = "Subscription ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        SubscriptionResponseDTO subscription = subscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(subscription);
    }

    @Operation(
            summary = "Create a new subscription",
            description = "Create a new subscription for a user with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or business rule violation",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "User already has active subscription for this plan",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> createSubscription(
            @Parameter(description = "Subscription data to create", required = true)
            @Valid @RequestBody CreateSubscriptionRequestDTO createSubscriptionDTO
    ) {
        SubscriptionResponseDTO createdSubscription = subscriptionService.createSubscription(createSubscriptionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscription);
    }

    @Operation(
            summary = "Update an existing subscription",
            description = "Update subscription information. Only provided fields will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> updateSubscription(
            @Parameter(description = "Subscription ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated subscription data", required = true)
            @Valid @RequestBody UpdateSubscriptionRequestDTO updateSubscriptionDTO
    ) {
        SubscriptionResponseDTO updatedSubscription = subscriptionService.updateSubscription(id, updateSubscriptionDTO);
        return ResponseEntity.ok(updatedSubscription);
    }

    @Operation(
            summary = "Delete a subscription",
            description = "Permanently remove a subscription from the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription deleted successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubscription(
            @Parameter(description = "Subscription ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        subscriptionService.deleteSubscription(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Subscription deleted successfully");
        response.put("subscriptionId", id.toString());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cancel a subscription",
            description = "Cancel a subscription by setting its status to CANCELLED"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription cancelled successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionResponseDTO> cancelSubscription(
            @Parameter(description = "Subscription ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        SubscriptionResponseDTO cancelledSubscription = subscriptionService.cancelSubscription(id);
        return ResponseEntity.ok(cancelledSubscription);
    }

    @Operation(
            summary = "Get subscriptions by user ID",
            description = "Retrieve all subscriptions for a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User subscriptions retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionResponseDTO>> getSubscriptionsByUserId(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId
    ) {
        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getSubscriptionsByUserId(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @Operation(
            summary = "Get active subscriptions by user ID",
            description = "Retrieve all active subscriptions for a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active user subscriptions retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<SubscriptionResponseDTO>> getActiveSubscriptionsByUserId(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId
    ) {
        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getActiveSubscriptionsByUserId(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @Operation(
            summary = "Get subscriptions by status",
            description = "Retrieve all subscriptions with a specific status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid status parameter",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SubscriptionResponseDTO>> getSubscriptionsByStatus(
            @Parameter(description = "Subscription status", required = true,
                    schema = @Schema(allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "EXPIRED", "CANCELLED"}),
                    example = "ACTIVE")
            @PathVariable SubscriptionStatus status
    ) {
        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getSubscriptionsByStatus(status);
        return ResponseEntity.ok(subscriptions);
    }

    @Operation(
            summary = "Get subscription statistics",
            description = "Get comprehensive statistics about subscriptions in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getSubscriptionStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalActive", subscriptionService.getSubscriptionCountByStatus(SubscriptionStatus.ACTIVE));
        stats.put("totalInactive", subscriptionService.getSubscriptionCountByStatus(SubscriptionStatus.INACTIVE));
        stats.put("totalExpired", subscriptionService.getSubscriptionCountByStatus(SubscriptionStatus.EXPIRED));
        stats.put("totalCancelled", subscriptionService.getSubscriptionCountByStatus(SubscriptionStatus.CANCELLED));
        stats.put("totalSuspended", subscriptionService.getSubscriptionCountByStatus(SubscriptionStatus.SUSPENDED));

        // Calculate current month revenue
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        BigDecimal monthlyRevenue = subscriptionService.calculateRevenue(startOfMonth, endOfMonth);
        stats.put("currentMonthRevenue", monthlyRevenue);

        return ResponseEntity.ok(stats);
    }

    // ========== PRIVATE HELPER METHODS ==========
    private Pageable createPageable(int page, int size, String[] sort) {
        Sort.Order[] orders = new Sort.Order[sort.length];
        for (int i = 0; i < sort.length; i++) {
            String[] sortParams = sort[i].split(",");
            String field = sortParams[0];
            String direction = sortParams.length > 1 ? sortParams[1] : "asc";
            orders[i] = new Sort.Order(Sort.Direction.fromString(direction), field);
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }
}
