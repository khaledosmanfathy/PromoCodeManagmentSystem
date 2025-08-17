package org.rakuten.service;

import lombok.RequiredArgsConstructor;
import org.rakuten.model.PromoCode;
import org.rakuten.model.PromoCodeStatus;
import org.rakuten.repository.PromoCodeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    @CacheEvict(value = "promo-codes", allEntries = true)
    public PromoCode createPromoCode(PromoCode promoCode) {
        if (promoCodeRepository.existsByCode(promoCode.getCode())) {
            throw new IllegalArgumentException("Promo code already exists");
        }
        return promoCodeRepository.save(promoCode);
    }

    @CacheEvict(value = "promo-codes", key = "#id")
    public PromoCode updatePromoCode(Long id, PromoCode updatedPromoCode) {
        PromoCode existing = getPromoCodeById(id);
        
        if (!existing.getCode().equals(updatedPromoCode.getCode())) {
            throw new IllegalArgumentException("Promo code cannot be changed");
        }
        
        existing.setAmount(updatedPromoCode.getAmount());
        existing.setDiscountType(updatedPromoCode.getDiscountType());
        existing.setExpiryDate(updatedPromoCode.getExpiryDate());
        existing.setUsageLimit(updatedPromoCode.getUsageLimit());
        existing.setStatus(updatedPromoCode.getStatus());
        
        return promoCodeRepository.save(existing);
    }

    @CacheEvict(value = "promo-codes", key = "#id")
    public void deletePromoCode(Long id) {
        PromoCode promoCode = getPromoCodeById(id);
        promoCodeRepository.delete(promoCode);
    }

    @Cacheable(value = "promo-codes", key = "#id")
    public PromoCode getPromoCodeById(Long id) {
        return promoCodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promo code not found"));
    }

    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    public List<PromoCode> getActivePromoCodes() {
        return promoCodeRepository.findByStatus(PromoCodeStatus.ACTIVE);
    }

    public List<PromoCode> getExpiredPromoCodes() {
        return promoCodeRepository.findByExpiryDateBefore(LocalDateTime.now());
    }

    @Cacheable(value = "promo-code-search", key = "{#code, #status, #startDate, #endDate}")
    public List<PromoCode> searchPromoCodes(String code, PromoCodeStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<PromoCode> spec = Specification.where(null);

        if (code != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("code"), "%" + code + "%"));
        }

        if (status != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
        }

        if (startDate != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate));
        }

        if (endDate != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate));
        }

        return promoCodeRepository.findAll(spec);
    }

    @Transactional
    public PromoCode applyPromoCode(String code) {
        PromoCode promoCode = promoCodeRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid promo code"));
        
        if (!promoCode.canBeUsed()) {
            throw new IllegalStateException("Promo code cannot be used");
        }
        
        promoCode.incrementUsage();
        return promoCodeRepository.save(promoCode);
    }
}