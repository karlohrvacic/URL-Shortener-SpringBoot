package me.oncut.urlshortener.service;

import me.oncut.urlshortener.dto.PasswordResetDto;
import me.oncut.urlshortener.dto.UpdatePasswordDto;
import me.oncut.urlshortener.dto.UserRegisterDto;
import me.oncut.urlshortener.dto.UserUpdateDto;
import me.oncut.urlshortener.model.User;
import java.util.List;

public interface UserService {

    User register(UserRegisterDto user);

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
}
