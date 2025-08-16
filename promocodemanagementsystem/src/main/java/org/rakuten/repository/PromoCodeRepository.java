package org.rakuten.repository;

import org.rakuten.model.PromoCode;
import org.rakuten.model.PromoCodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    List<PromoCode> findByStatus(PromoCodeStatus status);
    
    List<PromoCode> findByExpiryDateBefore(Date date);
    
    @Query("SELECT p FROM PromoCode p WHERE " +
           "(:code IS NULL OR p.code LIKE %:code%) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:startDate IS NULL OR p.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR p.createdAt <= :endDate)")
    List<PromoCode> searchPromoCodes(
            String code, 
            PromoCodeStatus status,
            Date startDate, 
            Date endDate);
    
    boolean existsByCode(String code);

    Optional<PromoCode> findByCode(String code);
}