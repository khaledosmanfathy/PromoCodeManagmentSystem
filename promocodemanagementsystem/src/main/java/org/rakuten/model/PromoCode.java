package org.rakuten.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must be less than 50 characters")
    @Column(nullable = false, unique = true)
    private String code;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Discount type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @PositiveOrZero(message = "Usage limit must be positive or zero")
    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usage_count")
    @Builder.Default
    private Integer usageCount = 0;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PromoCodeStatus status = PromoCodeStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate) || status == PromoCodeStatus.EXPIRED;
    }

    public boolean canBeUsed() {
        return status == PromoCodeStatus.ACTIVE && 
               !isExpired() && 
               (usageLimit == null || usageCount < usageLimit);
    }

    public void incrementUsage() {
        this.usageCount++;
        if (usageLimit != null && usageCount >= usageLimit) {
            this.status = PromoCodeStatus.EXPIRED;
        }
    }
}

