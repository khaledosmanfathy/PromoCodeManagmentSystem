package org.rakuten.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.rakuten.model.PromoCode;
import org.rakuten.model.PromoCodeStatus;
import org.rakuten.service.PromoCodeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/promo-codes")
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromoCode> createPromoCode(@Valid @RequestBody PromoCode promoCode) {
        return ResponseEntity.ok(promoCodeService.createPromoCode(promoCode));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromoCode> updatePromoCode(
            @PathVariable Long id, 
            @Valid @RequestBody PromoCode promoCode) {
        return ResponseEntity.ok(promoCodeService.updatePromoCode(id, promoCode));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePromoCode(@PathVariable Long id) {
        promoCodeService.deletePromoCode(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS')")
    public ResponseEntity<PromoCode> getPromoCodeById(@PathVariable Long id) {
        return ResponseEntity.ok(promoCodeService.getPromoCodeById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS')")
    public ResponseEntity<List<PromoCode>> getAllPromoCodes() {
        return ResponseEntity.ok(promoCodeService.getAllPromoCodes());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS')")
    public ResponseEntity<List<PromoCode>> getActivePromoCodes() {
        return ResponseEntity.ok(promoCodeService.getActivePromoCodes());
    }

    @GetMapping("/expired")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS')")
    public ResponseEntity<List<PromoCode>> getExpiredPromoCodes() {
        return ResponseEntity.ok(promoCodeService.getExpiredPromoCodes());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS')")
    public ResponseEntity<List<PromoCode>> searchPromoCodes(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) PromoCodeStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        return ResponseEntity.ok(promoCodeService.searchPromoCodes(code, status, startDate, endDate));
    }

    @GetMapping("/by-code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS')")
    public ResponseEntity<PromoCode> getPromoCodeByCode(@PathVariable String code) {
        return ResponseEntity.ok(promoCodeService.getPromoCodeByCode(code));
    }

    @PostMapping("/apply/{code}")
    public ResponseEntity<PromoCode> applyPromoCode(@PathVariable String code) {
        return ResponseEntity.ok(promoCodeService.applyPromoCode(code));
    }
}