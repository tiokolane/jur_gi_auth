package com.tiokolane.jur_gi_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiokolane.jur_gi_auth.model.PasswordResetToken;


public interface PasswordRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional <PasswordResetToken> findByToken(String token);
}
