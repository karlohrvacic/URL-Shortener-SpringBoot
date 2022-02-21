package codes.karlo.api.service;

import codes.karlo.api.entity.User;
import codes.karlo.api.exception.EmailExistsException;

import java.util.List;

public interface UserService {

    User register(User user) throws EmailExistsException;

    List<User> fetchUsers();

}
