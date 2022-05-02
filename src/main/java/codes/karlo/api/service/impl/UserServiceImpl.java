package codes.karlo.api.service.impl;

import codes.karlo.api.converter.UserUpdateDtoToUserConverter;
import codes.karlo.api.dto.UserUpdateDto;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.model.User;
import codes.karlo.api.repository.AuthoritiesRepository;
import codes.karlo.api.repository.UserRepository;
import codes.karlo.api.service.UserService;
import codes.karlo.api.validator.AuthValidator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List<User> fetchUsers() {
        return userRepository.findAll();
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
                .orElseThrow(() -> new UserDoesntExistException("User doesn't exist"));

        if (!user.getIsActive()) throw new UserDoesntExistException("User is inactive, contact administrator");

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
