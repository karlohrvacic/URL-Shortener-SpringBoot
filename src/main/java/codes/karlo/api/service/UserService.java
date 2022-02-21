package codes.karlo.api.service;

import codes.karlo.api.entity.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> fetchUsers();

}
