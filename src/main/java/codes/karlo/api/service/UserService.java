package codes.karlo.api.service;

import codes.karlo.api.entity.User;
import codes.karlo.api.exception.EmailExistsException;
import codes.karlo.api.exception.UserDoesntExistException;

import java.util.List;

public interface UserService {

    User register(User user) throws EmailExistsException;

    List<User> fetchUsers();

    User getUserFromToken() throws UserDoesntExistException;

    User fetchCurrentUser() throws UserDoesntExistException;
}
