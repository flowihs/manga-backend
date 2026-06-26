package ru.github.happshop.auth.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.github.happshop.auth.dto.request.*;
import ru.github.happshop.auth.dto.response.UpdateAccountDataResponse;
import ru.github.happshop.auth.dto.response.UserResponse;
import ru.github.happshop.auth.entity.*;
import ru.github.happshop.auth.exception.auth.EmailAlreadyExistsException;
import ru.github.happshop.auth.exception.auth.HasActiveTokenException;
import ru.github.happshop.auth.exception.auth.InvalidLoginException;
import ru.github.happshop.auth.exception.auth.InvalidPasswordException;
import ru.github.happshop.auth.exception.auth.PasswordResetTokenNotFound;
import ru.github.happshop.auth.exception.auth.TokenExpiredException;
import ru.github.happshop.auth.exception.auth.TokenNotFoundException;
import ru.github.happshop.auth.exception.auth.UsernameAlreadyExistsException;
import ru.github.happshop.auth.exception.user.UserNotFoundException;
import ru.github.happshop.auth.repository.*;
import ru.github.happshop.common.error.exception.UserException;
import ru.github.happshop.mail.service.MailService;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RecoverCompromisedAccountTokenRepository recoverCompromisedAccountTokenRepository;
    private final MailService mailService;
    private final EmailConfirmTokenRepository emailConfirmTokenRepository;

    private static final Random RANDOM = new Random();

    @Transactional
    public UserResponse register(final RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        User user = User.builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(UserRole.USER)
            .enabled(true)
            .build();

        mailService.sendWelcomeEmail(request.getEmail(), request.getUsername());

        return toResponse(userRepository.save(user));
    }

    public UserResponse login(final LoginRequest request) {
        User user = userRepository
            .findByEmail(request.getLogin())
            .or(() -> userRepository.findByUsername(request.getLogin()))
            .orElseThrow(UserNotFoundException::new);

        if (user.getPassword() == null) {
            throw new InvalidLoginException();
        }

        if (
            !passwordEncoder.matches(request.getPassword(), user.getPassword())
        ) {
            throw new InvalidLoginException();
        }

        return toResponse(user);
    }

    @Transactional
    public void forgotPassword(final ForgotPasswordRequest dto) {
        Optional<User> userOptional = userRepository.findByEmail(
            dto.getEmail()
        );

        if (userOptional.isEmpty()) {
            simulateDelay();
            return;
        }

        User user = userOptional.get();

        List<PasswordResetToken> tokens =
            passwordResetTokenRepository.findByUserId(user.getId());

        Optional<PasswordResetToken> activeToken = tokens
            .stream()
            .filter(t -> t.getExpiredAt().after(new Date()))
            .findFirst();

        if (activeToken.isPresent()) {
            List<PasswordResetToken> expiredTokens = tokens
                .stream()
                .filter(t -> t.getExpiredAt().before(new Date()))
                .collect(Collectors.toList());

            if (!expiredTokens.isEmpty()) {
                passwordResetTokenRepository.deleteAll(expiredTokens);
            }

            throw new HasActiveTokenException();
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
            .token(token)
            .userId(user.getId())
            .expiredAt(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
            .build();

        passwordResetTokenRepository.save(passwordResetToken);

        mailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Transactional
    public boolean resetPassword(
        final String token,
        final ResetPasswordRequest dto
    ) {
        PasswordResetToken resetToken = passwordResetTokenRepository
            .findByToken(token)
            .orElseThrow(PasswordResetTokenNotFound::new);

        if (resetToken.getExpiredAt().before(new Date())) {
            throw new TokenExpiredException();
        }

        User user = findById(resetToken.getUserId());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        passwordResetTokenRepository.deleteByUserId(resetToken.getUserId());

        return true;
    }

    @Transactional
    public void generateConfirmEmailToken(
        final GenerateEmailConfirmTokenRequest request
    ) {
        String token = UUID.randomUUID().toString();

        User user = userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(UserNotFoundException::new);

        EmailConfirmToken emailConfirmToken = EmailConfirmToken.builder()
            .id(token)
            .userId(user.getId())
            .expiredAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .build();

        emailConfirmTokenRepository.save(emailConfirmToken);

        mailService.sendConfirmEmail(
            user.getEmail(),
            user.getUsername(),
            token
        );
    }

    @Transactional
    public boolean confirmEmailByToken(final String token) {
        EmailConfirmToken validToken = emailConfirmTokenRepository
            .findById(token)
            .orElseThrow(TokenNotFoundException::new);

        if (validToken.getExpiredAt().before(new Date())) {
            throw new TokenExpiredException();
        }

        User user = findById(validToken.getUserId());

        user.setEnabledMail(true);
        userRepository.save(user);

        emailConfirmTokenRepository.deleteById(token);

        return true;
    }

    @Transactional
    public void updatePassword(
        final Long userId,
        final UpdatePasswordRequest request,
        final boolean sendMail,
        final boolean needOldPassword
    ) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new UserException("Пароли должны совпадать", HttpStatus.BAD_REQUEST);
        }

        User user = findById(userId);

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new UserException("Новый пароль должен отличаться от старого", HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()) && needOldPassword) {
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        RecoverCompromisedAccountToken token =
            RecoverCompromisedAccountToken.builder()
                .id(UUID.randomUUID().toString())
                .userId(user.getId())
                .expiredAt(
                    new Date(
                        System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3
                    )
                )
                .build();
        recoverCompromisedAccountTokenRepository.save(token);

        if (sendMail) {
            mailService.sendUpdatePasswordEmail(user.getEmail(), token.getId());
        }
    }

    public UpdateAccountDataResponse updateAccountData(
        final UpdateAccountDataRequest request
    ) {
        User user = findById(request.getId());

        user.setUsername(user.getUsername());

        User updateduser = userRepository.save(user);

        return UpdateAccountDataResponse.builder()
            .username(updateduser.getUsername())
            .build();
    }

    public UserResponse getMyProfile(final Long userId) {
        User user = userRepository
                .findByIdWithSettings(userId)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.fromEntity(user);
    }

    public boolean checkPassword(Long userId, CheckPasswordRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        boolean result = passwordEncoder.matches(dto.getPassword(), user.getPassword());

        if (!result) {
            throw new InvalidPasswordException();
        }

        return true;
    }

    public void changeDeleteAccountStatus(Long userId) {
        User user = findById(userId);
        if (user.getDeletionDate() != null) {
            user.setDeletionDate(null);
        } else {
            Date futureDate = Date.from(Instant.now().plus(3, ChronoUnit.DAYS));
            user.setDeletionDate(futureDate);
        }
    }

    public boolean userIsOwnSettings(final Long settingsId, final Long userId) {
        User user = findById(userId);
        return Objects.equals(user.getSettings().getId(), settingsId);
    }

    public boolean isExistingToken(final String token) {
        passwordResetTokenRepository
            .findByToken(token)
            .orElseThrow(PasswordResetTokenNotFound::new);

        return true;
    }

    public User findById(final Long id) {
        return userRepository
            .findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    public User findByEmail(final String email) {
        return userRepository
            .findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
    }

    private void simulateDelay() {
        try {
            Thread.sleep(500 + RANDOM.nextInt(500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private UserResponse toResponse(final User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .settings(user.getSettings())
                .build();
    }
}
