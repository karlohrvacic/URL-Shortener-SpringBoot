package me.oncut.urlshortener.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.configuration.properties.AppProperties;
import me.oncut.urlshortener.converter.UserUpdateDtoToUserConverter;
import me.oncut.urlshortener.dto.PasswordResetDto;
import me.oncut.urlshortener.dto.RequestPasswordResetDto;
import me.oncut.urlshortener.dto.UpdatePasswordDto;
import me.oncut.urlshortener.dto.UserUpdateDto;
import me.oncut.urlshortener.exception.NoAuthorizationException;
import me.oncut.urlshortener.exception.UserDoesntExistException;
import me.oncut.urlshortener.model.ResetToken;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.UserRepository;
import me.oncut.urlshortener.service.ResetTokenService;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.AuthValidator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final UserUpdateDtoToUserConverter userUpdateDtoToUserConverter;
    private final AppProperties appProperties;
    private final DefaultSendingEmailService sendingEmailService;
    private final ResetTokenService resetTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthValidator authValidator;
    private final ObjectMapper objectMapper;


    @Override
    public User register(final User user) {
        final User savedUser = userRepository.save(user);
        sendingEmailService.sendWelcomeEmail(savedUser);

        return savedUser;
    }

    @Override
    public User getUserFromToken() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(username).orElse(null);
    }

    @Override
    public User fetchCurrentUser() {
        return Optional.ofNullable(getUserFromToken()).map(user -> {
            user.setPassword("hidden");
            return user;
        }).orElseThrow(() -> new NoAuthorizationException("Invalid credentials"));
    }

    @Override
    public User fetchUserFromEmail(final String email) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesntExistException("User not found"));

        if (Boolean.FALSE.equals(user.getActive()))
            throw new UserDoesntExistException("User is inactive, contact administrator");

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
    public void sendPasswordResetLinkToUser(final RequestPasswordResetDto requestPasswordResetDto) {
        final Optional<User> user = userRepository.findByEmail(requestPasswordResetDto.getEmail());

        if (user.isPresent()) {
            resetTokenService.deactivateActiveResetTokenIfExists(user.get());

            sendingEmailService.sendEmailForgotPassword(user.get(), resetTokenService.createTokenForUser(user.get()));
        }
    }

    @Override
    @Transactional
    public User resetPassword(final PasswordResetDto passwordResetDto) {
        final User user = userRepository.findByEmail(passwordResetDto.getEmail())
                .orElseThrow(() -> new NoAuthorizationException("Invalid credentials"));

        final ResetToken token = resetTokenService.getResetTokenFromUserAndToken(user, passwordResetDto.getToken());

        if (token.isActive()) {
            user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
            resetTokenService.deactivateAndSaveToken(token);

            return userRepository.save(user);
        }

        throw new NoAuthorizationException("Token expired");
    }

    @Override
    public void deactivateUnusedUserAccounts() {
        final List<User> users = userRepository.findByLastLoginIsLessThanEqualAndActiveTrue(LocalDateTime.now()
                .minusDays(appProperties.getDeactivateUserAccountAfterDays())).stream()
                .map(user -> {
                    user.setActive(false);
                    log.info(String.format("Deactivated user with id %d", user.getId()));
                    return user;
                })
                .toList();

        userRepository.saveAll(users);
        if (!users.isEmpty())log.info(String.format("Deactivated %d users", users.size()));
    }

    @Override
    public void userHasLoggedIn(final String email) {
        final User user = fetchUserFromEmail(email);
        user.userLoggedIn();
        persistUser(user);
    }

}
