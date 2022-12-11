package cc.hrva.urlshortener.service.impl;

import cc.hrva.urlshortener.converter.UserRegisterDtoToUserConverter;
import cc.hrva.urlshortener.converter.UserToUserDtoConverter;
import cc.hrva.urlshortener.exception.NoAuthorizationException;
import cc.hrva.urlshortener.validator.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import cc.hrva.urlshortener.beans.JwtFilter;
import cc.hrva.urlshortener.beans.TokenProvider;
import cc.hrva.urlshortener.dto.JWTTokenDto;
import cc.hrva.urlshortener.dto.LoginDto;
import cc.hrva.urlshortener.dto.UserRegisterDto;
import cc.hrva.urlshortener.service.AuthService;
import cc.hrva.urlshortener.service.LoginAttemptService;
import cc.hrva.urlshortener.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {

    private final UserService userService;
    private final UserValidator userValidator;
    private final TokenProvider tokenProvider;
    private final LoginAttemptService loginAttemptService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRegisterDtoToUserConverter userRegisterDtoToUserConverter;

    @Override
    public String register(final UserRegisterDto userRegisterDto) {
        userValidator.checkEmailUniqueness(userRegisterDto.getEmail());

        final var user = userRegisterDtoToUserConverter.convert(userRegisterDto);
        return userService.register(user).getEmail();
    }

    @Override
    public ResponseEntity<JWTTokenDto> login(final LoginDto loginDto, final HttpServletRequest request) {
        if (loginAttemptService.isBlocked(getClientIP(request))) {
            throw new NoAuthorizationException("Request has been blocked");
        }
        final var token = getToken(loginDto);
        final var httpHeaders = getHttpHeaders(token);
        final var user = userService.fetchUserFromEmail(loginDto.getEmail());
        userService.userHasLoggedIn(user);
        final var jwtTokenDto = new JWTTokenDto(token, userToUserDtoConverter.convert(user));

        return new ResponseEntity<>(jwtTokenDto, httpHeaders, HttpStatus.OK);
    }

    @Override
    public String getClientIP(final HttpServletRequest request) {
        var ipAddress = "";
        if (request != null) {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (StringUtils.isEmpty(ipAddress) || "".equals(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
        }

        return ipAddress;
    }

    private String getToken(final LoginDto loginDto) {
        final var authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        );

        final var authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "Bearer ".concat(tokenProvider.createToken(authentication));
    }

    private HttpHeaders getHttpHeaders(final String token) {
        final var httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, token);

        return httpHeaders;
    }

}
