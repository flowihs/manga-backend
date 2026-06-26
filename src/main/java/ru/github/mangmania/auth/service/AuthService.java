package ru.github.mangmania.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.github.mangmania.auth.dto.request.LoginRequest;
import ru.github.mangmania.auth.dto.request.RegisterRequest;
import ru.github.mangmania.auth.dto.request.UpdatePasswordRequest;
import ru.github.mangmania.auth.dto.response.AuthResponse;
import ru.github.mangmania.auth.entity.RecoverCompromisedAccountToken;
import ru.github.mangmania.auth.entity.RefreshToken;
import ru.github.mangmania.auth.entity.User;
import ru.github.mangmania.auth.exception.auth.InvalidTokenTypeException;
import ru.github.mangmania.auth.exception.auth.RefreshTokenExpiredException;
import ru.github.mangmania.auth.exception.auth.TokenExpiredException;
import ru.github.mangmania.auth.exception.auth.TokenNotFoundException;
import ru.github.mangmania.auth.repository.RecoverCompromisedAccountTokenRepository;
import ru.github.mangmania.auth.repository.RefreshTokenRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RecoverCompromisedAccountTokenRepository recoverCompromisedAccountTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Transactional
    public AuthResponse refresh(final String refreshToken) {
        Date expiration = jwtService.extractExpiration(refreshToken);

        if (expiration.before(new Date())) {
            throw new RefreshTokenExpiredException();
        }

        String type = jwtService.extractTokenType(refreshToken);

        if (!"refresh".equals(type)) {
            throw new InvalidTokenTypeException();
        }

        Long userId = jwtService.extractUserId(refreshToken);

        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(InvalidTokenTypeException::new);
        refreshTokenRepository.deleteById(refreshToken);

        String newAccess = jwtService.generateAccessToken(userId);
        String newRefresh = jwtService.generateRefreshToken(userId);

        RefreshToken newEntity = RefreshToken.builder()
                .token(newRefresh)
                .userId(userId)
                .expiredAt(new Date(System.currentTimeMillis() + refreshExpiration))
                .build();
        refreshTokenRepository.save(newEntity);

        return new AuthResponse(newAccess, newRefresh);
    }

    public AuthResponse login(final LoginRequest request) {
        var user = userService.login(request);

        String access = jwtService.generateAccessToken(user.getId());
        String refresh = jwtService.generateRefreshToken(user.getId());

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refresh)
                .userId(user.getId())
                .expiredAt(new Date(System.currentTimeMillis() + refreshExpiration))
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return new AuthResponse(access, refresh);
    }

    public AuthResponse register(final RegisterRequest request) {
        var user = userService.register(request);

        String access = jwtService.generateAccessToken(user.getId());
        String refresh = jwtService.generateRefreshToken(user.getId());

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refresh)
                .userId(user.getId())
                .expiredAt(new Date(System.currentTimeMillis() + refreshExpiration))
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return new AuthResponse(access, refresh);
    }

    @Transactional
    public void recoverCompromisedAccount(final String token, final UpdatePasswordRequest request) {
        RecoverCompromisedAccountToken tokenEntity = recoverCompromisedAccountTokenRepository.findById(token)
                .orElseThrow(TokenNotFoundException::new);

        if (tokenEntity.getExpiredAt().before(new Date())) {
            throw new TokenExpiredException();
        }

        User user = userService.findById(tokenEntity.getUserId());

        recoverCompromisedAccountTokenRepository.delete(tokenEntity);

        userService.updatePassword(user.getId(), request, false, false);

        logoutUser(user.getId());
    }

    public void logout(final String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    @Transactional
    public void logoutUser(final Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
