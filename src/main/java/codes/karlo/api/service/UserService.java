package codes.karlo.api.service;

import codes.karlo.api.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> fetchUsers();

    User getUserFromToken();

    User fetchCurrentUser();

    User fetchUserFromEmail(String email);

    void persistUser(User user);

    Page<User> fetchAllUsers(int page, int size);

    void deleteUserById(Long id);
}
