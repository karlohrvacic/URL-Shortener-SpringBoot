package codes.karlo.api.service.impl;

import codes.karlo.api.entity.User;
import codes.karlo.api.exception.EmailExistsException;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.repository.AuthoritiesRepository;
import codes.karlo.api.repository.UserRepository;
import codes.karlo.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Override
    public User register(User user) throws EmailExistsException {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setAuthorities(List.of(Objects.requireNonNull(
                authoritiesRepository
                        .findByName("ROLE_USER")
                        .orElse(null))));

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new EmailExistsException("Email already exists!");
        }
    }

    @Override
    public List<User> fetchUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserFromToken() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(userDetails.getUsername())
                .orElse(null);
    }

    @Override
    public User fetchCurrentUser() {
        User user = getUserFromToken();

        user.setPassword("hidden");

        return user;
    }

    @Override
    public User fetchUserFromEmail(String email) throws UserDoesntExistException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesntExistException("User not found in the database"));
    }

    @Override
    public void persistUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Page<User> fetchAllUsers(final int page, final int size) {

        final Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return userRepository.findAll(pageable);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


}
