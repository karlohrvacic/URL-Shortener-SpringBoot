package cc.hrva.urlshortener.controller;

import cc.hrva.urlshortener.dto.UpdatePasswordDto;
import cc.hrva.urlshortener.dto.UserDto;
import cc.hrva.urlshortener.dto.UserUpdateDto;
import cc.hrva.urlshortener.model.User;
import cc.hrva.urlshortener.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> currentUser() {
        return ResponseEntity.ok(userService.fetchCurrentUser());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> fetchAllUsers() {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") final Long id) {
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@Valid @RequestBody final UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.updateUser(userUpdateDto));
    }

    @PutMapping("/update-password")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<User> setPassword(@Valid @RequestBody final UpdatePasswordDto updatePasswordDto) {
        return ResponseEntity.ok(userService.updatePassword(updatePasswordDto));
    }

}
