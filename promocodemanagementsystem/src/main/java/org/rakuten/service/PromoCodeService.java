package org.rakuten.service;

import lombok.RequiredArgsConstructor;
import org.rakuten.model.PromoCode;
import org.rakuten.model.PromoCodeStatus;
import org.rakuten.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCode createPromoCode(PromoCode promoCode) {
        if (promoCodeRepository.existsByCode(promoCode.getCode())) {
            throw new IllegalArgumentException("Promo code already exists");
        }
        return promoCodeRepository.save(promoCode);
    }

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

    public void deletePromoCode(Long id) {
        PromoCode promoCode = getPromoCodeById(id);
        promoCodeRepository.delete(promoCode);
    }

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
        return promoCodeRepository.findByExpiryDateBefore(new Date());
    }

    public List<PromoCode> searchPromoCodes(String code, PromoCodeStatus status, Date startDate, Date endDate) {
        return promoCodeRepository.searchPromoCodes(code, status, startDate, endDate);
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