package org.rakuten.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

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
    private DiscountType discountType;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private Date expiryDate;

    @PositiveOrZero(message = "Usage limit must be positive or zero")
    private Integer usageLimit;

    @Builder.Default
    private Integer usageCount = 0;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PromoCodeStatus status = PromoCodeStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public boolean isExpired() {
        return new Date().after(expiryDate) || status == PromoCodeStatus.EXPIRED;
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

