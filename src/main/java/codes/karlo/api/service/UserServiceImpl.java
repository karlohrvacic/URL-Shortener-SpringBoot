package codes.karlo.api.service;

import codes.karlo.api.entity.User;
import codes.karlo.api.exception.EmailExistsException;
import codes.karlo.api.repository.AuthoritiesRepository;
import codes.karlo.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthoritiesRepository authoritiesRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) throws EmailExistsException {


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setAuthorities(List.of(Objects.requireNonNull(
                authoritiesRepository
                        .findByName("USER")
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
}
