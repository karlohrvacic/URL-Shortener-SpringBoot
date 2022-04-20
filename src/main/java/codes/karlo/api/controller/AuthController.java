package codes.karlo.api.controller;

import codes.karlo.api.config.JwtFilter;
import codes.karlo.api.config.TokenProvider;
import codes.karlo.api.dto.JWTTokenDto;
import codes.karlo.api.dto.LoginDto;
import codes.karlo.api.entity.User;
import codes.karlo.api.exception.EmailExistsException;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CommonsLog
@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin("${app.frontend-url}")
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public AuthController(final UserService userService, final TokenProvider tokenProvider, final AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Operation(summary = "Register user")
    @PostMapping("/register")
    public User register(@Valid @RequestBody final User user) throws EmailExistsException {
        log.info("Register controller invoked for user " + user);
        return userService.register(user);
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<JWTTokenDto> fetchUrlByShort(@Valid @RequestBody final LoginDto login) throws UserDoesntExistException {

        log.info("Login controller invoked for user " + login.getEmail());

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                login.getEmail(),
                login.getPassword()
        );

        final Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String jwt = tokenProvider.createToken(authentication);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);


        final User user = userService.fetchUserFromEmail(login.getEmail());
        user.userLoggedIn();
        userService.persistUser(user);

        log.info("Issuing a token for " + user);

        return new ResponseEntity<>(new JWTTokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

}
