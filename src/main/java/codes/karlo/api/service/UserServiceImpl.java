package codes.karlo.api.service;

import codes.karlo.api.entity.User;
import codes.karlo.api.repository.AuthoritiesRepository;
import codes.karlo.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthoritiesRepository authoritiesRepository) {
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    @Override
    public User register(User user) {
        user.setAuthorities(List.of(Objects.requireNonNull(
                authoritiesRepository
                        .findByName("USER")
                        .orElse(null))));
        return userRepository.save(user);
    }

    @Override
    public List<User> fetchUsers() {
        return userRepository.findAll();
    }
}
