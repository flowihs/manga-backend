package ru.github.mangmania.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.github.mangmania.auth.entity.PasswordResetToken;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUserId(Long userId);

    List<PasswordResetToken> findByUserId(Long userId);
}
