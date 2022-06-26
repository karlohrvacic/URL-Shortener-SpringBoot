package me.oncut.urlshortener.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.dto.JWTTokenDto;
import me.oncut.urlshortener.dto.LoginDto;
import me.oncut.urlshortener.dto.PasswordResetDto;
import me.oncut.urlshortener.dto.RequestPasswordResetDto;
import me.oncut.urlshortener.dto.UserRegisterDto;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.service.AuthService;
import me.oncut.urlshortener.service.UserService;
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
    public String register(@Valid @RequestBody final UserRegisterDto user) {
        log.info("Register controller invoked for user " + user.getEmail());
        return authService.register(user);
    }

    @PostMapping("/reset-password")
    public void requestPasswordReset(@RequestBody final RequestPasswordResetDto requestPasswordResetDto) {
        log.info("Forgot password controller invoked for " + requestPasswordResetDto.getEmail());
        userService.sendPasswordResetLinkToUser(requestPasswordResetDto);
    }

    @PostMapping("/reset-password/set-password")
    public User resetPassword(@Valid @RequestBody final PasswordResetDto passwordResetDto) {
        log.info("Forgot password controller invoked for " + passwordResetDto.getEmail());
        return userService.resetPassword(passwordResetDto);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDto> login(@Valid @RequestBody final LoginDto login) {
        log.info("Login controller invoked for user " + login.getEmail());
        return authService.login(login, request);
    }
}
