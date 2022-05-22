package me.oncut.urlshortener.controller;

import me.oncut.urlshortener.dto.UpdatePasswordDto;
import me.oncut.urlshortener.dto.UserUpdateDto;
import me.oncut.urlshortener.exception.UserDoesntExistException;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.service.UserService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin("${app.frontend-url}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/whoAmI")
    public User currentUser() throws UserDoesntExistException {
        return userService.fetchCurrentUser();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> fetchAllUsers() {
        return userService.fetchAllUsers();
    }

    @DeleteMapping({"/{id}"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable("id") final Long id) {
        userService.deleteUserById(id);
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User updateUser(@Valid @RequestBody final UserUpdateDto userUpdateDto) {
        return userService.updateUser(userUpdateDto);
    }

    @PutMapping("/update-password")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public User setPassword(@Valid @RequestBody final UpdatePasswordDto updatePasswordDto) {
        return userService.updatePassword(updatePasswordDto);
    }
}
