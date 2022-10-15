package me.oncut.urlshortener.service.impl;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import me.oncut.urlshortener.beans.JwtFilter;
import me.oncut.urlshortener.beans.TokenProvider;
import me.oncut.urlshortener.converter.UserRegisterDtoToUserConverter;
import me.oncut.urlshortener.dto.JWTTokenDto;
import me.oncut.urlshortener.dto.LoginDto;
import me.oncut.urlshortener.dto.UserRegisterDto;
import me.oncut.urlshortener.exception.NoAuthorizationException;
import me.oncut.urlshortener.service.AuthService;
import me.oncut.urlshortener.service.LoginAttemptService;
import me.oncut.urlshortener.service.UserService;
import me.oncut.urlshortener.validator.UserValidator;
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

        userService.userHasLoggedIn(loginDto.getEmail());

        return new ResponseEntity<>(new JWTTokenDto(token), httpHeaders, HttpStatus.OK);
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

        return tokenProvider.createToken(authentication);
    }

    private HttpHeaders getHttpHeaders(final String token) {
        final var httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token);

        return httpHeaders;
    }

}
