package cc.hrva.urlshortener.service.impl;

import cc.hrva.urlshortener.configuration.properties.AppProperties;
import cc.hrva.urlshortener.converter.UserToUserDtoConverter;
import cc.hrva.urlshortener.converter.UserUpdateDtoToUserConverter;
import cc.hrva.urlshortener.dto.PasswordResetDto;
import cc.hrva.urlshortener.dto.RequestPasswordResetDto;
import cc.hrva.urlshortener.dto.UpdatePasswordDto;
import cc.hrva.urlshortener.dto.UserDto;
import cc.hrva.urlshortener.dto.UserUpdateDto;
import cc.hrva.urlshortener.exception.NoAuthorizationException;
import cc.hrva.urlshortener.exception.UserDoesntExistException;
import cc.hrva.urlshortener.model.User;
import cc.hrva.urlshortener.repository.UserRepository;
import cc.hrva.urlshortener.service.ResetTokenService;
import cc.hrva.urlshortener.service.UserService;
import cc.hrva.urlshortener.validator.AuthValidator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final AppProperties appProperties;
    private final AuthValidator authValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetTokenService resetTokenService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final DefaultSendingEmailService sendingEmailService;
    private final UserUpdateDtoToUserConverter userUpdateDtoToUserConverter;

    @Override
    public User register(final User user) {
        final var savedUser = userRepository.save(user);
        sendingEmailService.sendWelcomeEmail(savedUser);

        return savedUser;
    }

    @Override
    public User getUserFromToken() {
        final var username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(username).orElse(null);
    }

    @Override
    public UserDto fetchCurrentUser() {
        return Optional.ofNullable(getUserFromToken())
                .map(userToUserDtoConverter::convert)
                .orElseThrow(() -> new NoAuthorizationException("Invalid credentials"));
    }

    @Override
    public User fetchUserFromEmail(final String email) {
        final var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesntExistException("User not found"));

        if (Boolean.FALSE.equals(user.getActive()))
            throw new UserDoesntExistException("User is inactive, please contact administrator");

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
        final var user = getUserFromToken();
        authValidator.passwordMatchesCurrentPassword(user, updatePasswordDto.getOldPassword());
        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void sendPasswordResetLinkToUser(final RequestPasswordResetDto requestPasswordResetDto) {
        final var user = userRepository.findByEmail(requestPasswordResetDto.getEmail());

        if (user.isPresent()) {
            resetTokenService.deactivateActiveResetTokenIfExists(user.get());
            sendingEmailService.sendEmailForgotPassword(user.get(), resetTokenService.createTokenForUser(user.get()));
        }
    }

    @Override
    @Transactional
    public User resetPassword(final PasswordResetDto passwordResetDto) {
        final var user = userRepository.findByEmail(passwordResetDto.getEmail())
                .orElseThrow(() -> new NoAuthorizationException("Invalid credentials"));

        final var token = resetTokenService.getResetTokenFromUserAndToken(user, passwordResetDto.getToken());

        if (token.isActive()) {
            user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
            resetTokenService.deactivateAndSaveToken(token);

            return userRepository.save(user);
        }

        throw new NoAuthorizationException("Token expired");
    }

    @Override
    public void deactivateUnusedUserAccounts() {
        final var users = userRepository.findByLastLoginIsLessThanEqualAndActiveTrue(LocalDateTime.now()
                .minusDays(appProperties.getDeactivateUserAccountAfterDays())).stream()
                .map(this::deactivateUser)
                .toList();

        userRepository.saveAll(users);
        if (!users.isEmpty())log.info(String.format("Deactivated %d users", users.size()));
    }

    @Override
    public void userHasLoggedIn(final User user) {
        user.userLoggedIn();
        persistUser(user);
    }

    private User deactivateUser(final User user) {
        user.setActive(false);
        log.info(String.format("Deactivated user with id %d", user.getId()));

        return user;
    }

}
