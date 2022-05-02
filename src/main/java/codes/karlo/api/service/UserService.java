package codes.karlo.api.service;

import codes.karlo.api.dto.UserUpdateDto;
import codes.karlo.api.model.User;
import java.util.List;

public interface UserService {

    User register(User user);

    List<User> fetchUsers();

    User getUserFromToken();

    User fetchCurrentUser();

    User fetchUserFromEmail(String email);

    void persistUser(User user);

    List<User> fetchAllUsers();

    void deleteUserById(Long id);

    User updateUser(UserUpdateDto userUpdateDto);
}
