package me.oncut.urlshortener.service.impl;

import me.oncut.urlshortener.converter.UserUpdateDtoToUserConverter;
import me.oncut.urlshortener.dto.UserUpdateDto;
import me.oncut.urlshortener.exception.UserDoesntExistException;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.repository.AuthoritiesRepository;
import me.oncut.urlshortener.repository.UserRepository;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.AuthValidator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final AuthValidator authValidator;
    private final UserUpdateDtoToUserConverter userUpdateDtoToUserConverter;

    @Override
    public User register(final User user) {
        authValidator.emailUniqueness(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthorities(List.of(Objects.requireNonNull(authoritiesRepository.findByName("ROLE_USER")
                .orElse(null))));

        return userRepository.save(user);
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
    public User updateUser(final UserUpdateDto userUpdateDto) {
        return userRepository.save(Objects.requireNonNull(userUpdateDtoToUserConverter.convert(userUpdateDto)));
    }

}
