package codes.karlo.api.service;

import codes.karlo.api.entity.User;
import codes.karlo.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) {
        return null;
    }

    @Override
    public User login(User user) {
        return null;
    }

}
