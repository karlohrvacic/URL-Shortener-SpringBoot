package cc.hrva.urlshortener.service;

import java.util.List;
import cc.hrva.urlshortener.dto.PasswordResetDto;
import cc.hrva.urlshortener.dto.RequestPasswordResetDto;
import cc.hrva.urlshortener.dto.UpdatePasswordDto;
import cc.hrva.urlshortener.dto.UserUpdateDto;
import cc.hrva.urlshortener.model.User;

public interface UserService {

    User register(User user);

    User getUserFromToken();

    User fetchCurrentUser();

    User fetchUserFromEmail(String email);

    void persistUser(User user);

    List<User> fetchAllUsers();

    void deleteUserById(Long id);

    User updateUser(UserUpdateDto userUpdateDto);

    User updatePassword(UpdatePasswordDto updatePasswordDto);

    void sendPasswordResetLinkToUser(RequestPasswordResetDto requestPasswordResetDto);

    User resetPassword(PasswordResetDto passwordResetDto);

    void deactivateUnusedUserAccounts();

    void userHasLoggedIn(String email);
}
