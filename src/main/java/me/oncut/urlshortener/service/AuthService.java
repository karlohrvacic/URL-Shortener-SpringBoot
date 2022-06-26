package me.oncut.urlshortener.service;

import javax.servlet.http.HttpServletRequest;
import me.oncut.urlshortener.dto.JWTTokenDto;
import me.oncut.urlshortener.dto.LoginDto;
import me.oncut.urlshortener.dto.UserRegisterDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    String register(UserRegisterDto userRegisterDto);

    ResponseEntity<JWTTokenDto> login(LoginDto loginDto, HttpServletRequest request);

    String getClientIP(HttpServletRequest request);

}
