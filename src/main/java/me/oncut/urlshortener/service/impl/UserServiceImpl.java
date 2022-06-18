package me.oncut.urlshortener.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.config.AppProperties;
import me.oncut.urlshortener.converter.UserRegisterDtoToUserConverter;
import me.oncut.urlshortener.converter.UserUpdateDtoToUserConverter;
import me.oncut.urlshortener.dto.PasswordResetDto;
import me.oncut.urlshortener.dto.UpdatePasswordDto;
import me.oncut.urlshortener.dto.UserRegisterDto;
import me.oncut.urlshortener.dto.UserUpdateDto;
import me.oncut.urlshortener.exception.NoAuthorizationException;
import me.oncut.urlshortener.exception.UserDoesntExistException;
import me.oncut.urlshortener.model.ResetToken;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.AuthoritiesRepository;
import me.oncut.urlshortener.repository.ResetTokenRepository;
import me.oncut.urlshortener.repository.UserRepository;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.AuthValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommonsLog
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final AuthValidator authValidator;
    private final UserUpdateDtoToUserConverter userUpdateDtoToUserConverter;
    private final AppProperties appProperties;
    private final ResetTokenRepository resetTokenRepository;
    private final SendingEmailServiceImpl sendingEmailService;
    private final UserRegisterDtoToUserConverter userRegisterDtoToUserConverter;

    @Override
    public User register(final UserRegisterDto userRegisterDto) {
        final User user = userRegisterDtoToUserConverter.convert(userRegisterDto);

        authValidator.emailUniqueness(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthorities(List.of(Objects.requireNonNull(authoritiesRepository.findByName("ROLE_USER")
                .orElse(null))));
        user.setApiKeySlots(appProperties.getUserApiKeySlots());

        final User savedUser = userRepository.save(user);
        sendingEmailService.sendWelcomeEmail(savedUser);

        return savedUser;
    }

    @Override
    public User getUserFromToken() {
        final Authentication loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication();
        final String username = loggedInUser.getName();

        return userRepository.findByEmail(username)
                .orElse(null);
    }

    @Override
    public User fetchCurrentUser() {
        final User user = getUserFromToken();
        user.setPassword("hidden");
        return user;
    }

    @Override
    public User fetchUserFromEmail(final String email) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesntExistException("User not found"));

        if (Boolean.FALSE.equals(user.getActive())) throw new UserDoesntExistException("User is inactive, contact administrator");

        return user;
    }

    @Override
    public void persistUser(final User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User updateUser(final UserUpdateDto userUpdateDto) {
        return userRepository.save(Objects.requireNonNull(userUpdateDtoToUserConverter.convert(userUpdateDto)));
    }

    @Override
    @Transactional
    public User updatePassword(final UpdatePasswordDto updatePasswordDto) {
        final User user = getUserFromToken();

        authValidator.passwordMatchesCurrentPassword(user, updatePasswordDto.getOldPassword());

        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void sendPasswordResetLinkToUser(final String email) {
        final Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            deactivateActiveResetTokenIfExists(user.get());
            final ResetToken resetToken = ResetToken.builder()
                    .user(user.get())
                    .expirationDate(LocalDateTime.now().plusHours(appProperties.getResetTokenExpirationInHours()))
                    .build();
            final ResetToken savedResetToken = resetTokenRepository.save(resetToken);
            sendingEmailService.sendEmailForgotPassword(user.get(), savedResetToken);
        }
    }

    @Override
    @Transactional
    public User resetPassword(final PasswordResetDto passwordResetDto) {
        final User user = userRepository.findByEmail(passwordResetDto.getEmail())
                .orElseThrow(() -> new NoAuthorizationException("Invalid credentials"));
        final ResetToken token = resetTokenRepository.findResetTokenByUserAndTokenAndActiveTrue(user, passwordResetDto.getToken())
                .orElseThrow(() -> new NoAuthorizationException("Invalid credentials"));
        token.verifyResetTokenValidity();

        if (token.isActive()) {
           user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
           token.setActive(false);
           resetTokenRepository.save(token);
           return userRepository.save(user);
       }
       throw new NoAuthorizationException("Token expired");
    }

    @Override
    public void deactivateExpiredPasswordResetTokens() {
        final List<ResetToken> resetTokens = resetTokenRepository.findByActiveTrue();

        for (final ResetToken resetToken : resetTokens) {
            if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
                resetToken.setActive(false);
                resetTokenRepository.save(resetToken);
                log.info(String.format("Reset token with id %d deactivated", resetToken.getId()));
            }
        }
    }

    @Override
    public void deleteExpiredPasswordResetTokens() {
        final List<ResetToken> resetTokens = resetTokenRepository.findByActiveFalse();

        for (final ResetToken resetToken : resetTokens) {
            if (LocalDateTime.now().isAfter(resetToken.getExpirationDate().plusHours(appProperties.getIpRetentionDurationInHours()))) {
                resetTokenRepository.delete(resetToken);
                log.info(String.format("Reset token with id %d deleted", resetToken.getId()));
            }
        }
    }

    @Override
    public void deactivateUnusedUserAccounts() {
        final List<User> users = userRepository.findAll();

        for (final User user : users) {
            if (LocalDateTime.now().isAfter(user.getLastLogin().plusDays(appProperties.getDeactivateUserAccountAfterDays()))) {
                sendingEmailService.sendEmailAccountDeactivated(user);
                user.setActive(false);
                userRepository.save(user);
                log.info(String.format("User with id %d deactivated", user.getId()));
            }
        }
    }

    private void deactivateActiveResetTokenIfExists(final User user) {
        final List<ResetToken> resetTokens = resetTokenRepository.findByUserAndActiveTrue(user).stream()
                .map(resetToken -> {
                    resetToken.setActive(false);
                    return resetToken;
                })
                .toList();

        resetTokenRepository.saveAll(resetTokens);
    }

}
