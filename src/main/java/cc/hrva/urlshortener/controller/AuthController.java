package cc.hrva.urlshortener.controller;

import cc.hrva.urlshortener.dto.*;
import cc.hrva.urlshortener.model.User;
import cc.hrva.urlshortener.service.AuthService;
import cc.hrva.urlshortener.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CommonsLog
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final HttpServletRequest request;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody final UserRegisterDto user) {
        log.info("Register controller invoked for user " + user.getEmail());
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody final RequestPasswordResetDto requestPasswordResetDto) {
        log.info("Forgot password controller invoked for " + requestPasswordResetDto.getEmail());
        userService.sendPasswordResetLinkToUser(requestPasswordResetDto);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/reset-password/set-password")
    public ResponseEntity<User> resetPassword(@Valid @RequestBody final PasswordResetDto passwordResetDto) {
        log.info("Forgot password controller invoked for " + passwordResetDto.getEmail());
        return ResponseEntity.ok(userService.resetPassword(passwordResetDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDto> login(@Valid @RequestBody final LoginDto login) {
        log.info("Login controller invoked for user " + login.getEmail());
        return authService.login(login, request);
    }
}
