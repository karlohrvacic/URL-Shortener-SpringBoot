package cc.hrva.urlshortener.service;

import cc.hrva.urlshortener.dto.PasswordResetDto;
import cc.hrva.urlshortener.dto.RequestPasswordResetDto;
import cc.hrva.urlshortener.dto.UpdatePasswordDto;
import cc.hrva.urlshortener.dto.UserDto;
import cc.hrva.urlshortener.dto.UserUpdateDto;
import cc.hrva.urlshortener.model.User;
import java.util.List;

public interface UserService {

    User getUserFromToken();
    User register(User user);
    UserDto fetchCurrentUser();
    List<User> fetchAllUsers();
    void persistUser(User user);
    void deleteUserById(Long id);
    void userHasLoggedIn(User user);
    void deactivateUnusedUserAccounts();
    User fetchUserFromEmail(String email);
    User updateUser(UserUpdateDto userUpdateDto);
    User updatePassword(UpdatePasswordDto updatePasswordDto);
    User resetPassword(PasswordResetDto passwordResetDto);
    void sendPasswordResetLinkToUser(RequestPasswordResetDto requestPasswordResetDto);

}
