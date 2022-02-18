package codes.karlo.api.service;

import codes.karlo.api.entity.User;

public interface UserService {

    User register(User user);

    User login(User user);

}
