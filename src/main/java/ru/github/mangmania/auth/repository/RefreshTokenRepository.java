package ru.github.mangmania.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.github.mangmania.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByUserId(Long userId);
}
