package me.oncut.urlshortener.service;

import java.util.List;
import me.oncut.urlshortener.dto.PasswordResetDto;
import me.oncut.urlshortener.dto.UpdatePasswordDto;
import me.oncut.urlshortener.dto.UserUpdateDto;
import me.oncut.urlshortener.model.User;

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

    void sendPasswordResetLinkToUser(String email);

    User resetPassword(PasswordResetDto passwordResetDto);

    void deactivateUnusedUserAccounts();

    void userHasLoggedIn(String email);
}
