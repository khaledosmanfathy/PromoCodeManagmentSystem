package org.rakuten.repository;

import org.rakuten.model.PromoCode;
import org.rakuten.model.PromoCodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long>, JpaSpecificationExecutor<PromoCode> {

    List<PromoCode> findByStatus(PromoCodeStatus status);
    
    List<PromoCode> findByExpiryDateBefore(LocalDateTime date);
    
    boolean existsByCode(String code);

    Optional<PromoCode> findByCode(String code);
}