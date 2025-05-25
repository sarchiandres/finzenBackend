package com.FinZen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FinZen.models.Entities.PasswordResetToken;
import com.FinZen.models.Entities.Usuarios;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUsuario(Usuarios usuario);
    void deleteByUsuario(Usuarios usuario);
}
