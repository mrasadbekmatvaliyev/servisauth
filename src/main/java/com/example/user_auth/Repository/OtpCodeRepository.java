package com.example.user_auth.Repository;

import org.springframework.stereotype.Repository;

import com.example.user_auth.Model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findTopByUserIdOrderByIdDesc(Long userId);
    Optional<OtpCode> findTopByCodeAndIsUsedFalseOrderByIdDesc(String code);
}
