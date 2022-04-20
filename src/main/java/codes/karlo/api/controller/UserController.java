package codes.karlo.api.controller;

import codes.karlo.api.entity.User;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/user")
@CrossOrigin("${app.frontend-url}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> fetchUsers() {
        return userService.fetchUsers();
    }

    @GetMapping("/whoAmI")
    public User currentUser() throws UserDoesntExistException {
        return userService.fetchCurrentUser();
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> fetchAllUsers(@RequestParam(name = "page") final int page,
                                    @RequestParam(name = "size") final int size) {
        return userService.fetchAllUsers(page, size);
    }

    @DeleteMapping({"/{id}"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable("id") final Long id) {
        userService.deleteUserById(id);
    }

}
