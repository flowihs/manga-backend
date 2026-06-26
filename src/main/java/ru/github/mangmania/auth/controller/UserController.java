package ru.github.mangmania.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.github.mangmania.auth.dto.request.*;
import ru.github.mangmania.auth.dto.response.UpdateAccountDataResponse;
import ru.github.mangmania.auth.dto.response.UserResponse;
import ru.github.mangmania.auth.service.AuthService;
import ru.github.mangmania.auth.service.JwtService;
import ru.github.mangmania.auth.service.UserService;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/forgot-password")
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public boolean resetPassword(
            @RequestParam final String token,
            @Valid @RequestBody final ResetPasswordRequest request
    ) {
        return userService.resetPassword(token, request);
    }

    @GetMapping("/forgot-password/is-existing-token")
    public boolean isExistingToken(@RequestParam final String token) {
        return userService.isExistingToken(token);
    }

    @PostMapping("/generate-confirm-email-token")
    public void generateConfirmEmailToken(@Valid @RequestBody final GenerateEmailConfirmTokenRequest request) {
        userService.generateConfirmEmailToken(request);
    }

    @PostMapping("/confirm-email")
    public boolean confirmEmail(@RequestParam final String token) {
        return userService.confirmEmailByToken(token);
    }

    @PostMapping("/update-data")
    public ResponseEntity<UpdateAccountDataResponse> updateAccountData(
            @Valid @RequestBody final UpdateAccountDataRequest request) {
        return ResponseEntity.ok(userService.updateAccountData(request));
    }

    @PostMapping("/recover-compromised-account")
    public void recoverCompromisedAccount(@RequestParam final String token, @RequestBody final UpdatePasswordRequest request) {
        authService.recoverCompromisedAccount(token, request);
    }

    @PostMapping("/update-password")
    public void updatePassword(
            final HttpServletRequest httpRequest,
            @Valid @RequestBody final UpdatePasswordRequest request
    ) {
        Long userId = jwtService.getUserId(httpRequest);
        userService.updatePassword(userId, request, true, true);
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserResponse> getMyProfile(final HttpServletRequest request) {
        Long userId = jwtService.getUserId(request);
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }

    @PostMapping("/check-password")
    public ResponseEntity<Boolean> checkPassword(@RequestBody @Valid final CheckPasswordRequest request,
                                                 final HttpServletRequest httpRequest) {
        Long userId = jwtService.getUserId(httpRequest);
        return ResponseEntity.ok(userService.checkPassword(userId, request));
    }

    @PostMapping("/change-delete-status")
    public void changeDeleteAccountStatus(final HttpServletRequest request) {
        Long userId = jwtService.getUserId(request);
        userService.changeDeleteAccountStatus(userId);
    }
}
