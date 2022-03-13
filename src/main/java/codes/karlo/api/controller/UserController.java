package codes.karlo.api.controller;

import codes.karlo.api.entity.User;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/user")
@CrossOrigin("${frontend.url}")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
    }

}
