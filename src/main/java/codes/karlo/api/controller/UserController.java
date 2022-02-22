package codes.karlo.api.controller;

import codes.karlo.api.entity.User;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<User> fetchUsers() {
        return userService.fetchUsers();
    }

    @GetMapping("/whoAmI")
    public User currentUser() throws UserDoesntExistException {
        return userService.fetchCurrentUser();
    }

}
