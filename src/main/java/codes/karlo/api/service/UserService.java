package codes.karlo.api.service;

import codes.karlo.api.entity.User;
import codes.karlo.api.exception.EmailExistsException;
import codes.karlo.api.exception.UserDoesntExistException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    User register(User user) throws EmailExistsException;

    List<User> fetchUsers();

    User getUserFromToken() throws UserDoesntExistException;

    User fetchCurrentUser() throws UserDoesntExistException;

    User fetchUserFromEmail(String email) throws UserDoesntExistException;

    User persistUser(User user);

    Page<User> fetchAllUsers(int page, int size);

    void deleteUserById(Long id);
}
