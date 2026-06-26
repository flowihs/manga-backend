package ru.github.happshop.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.github.happshop.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByUserId(Long userId);
}
