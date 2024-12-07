package cc.hrva.urlshortener.service;

import cc.hrva.urlshortener.dto.JWTTokenDto;
import cc.hrva.urlshortener.dto.LoginDto;
import cc.hrva.urlshortener.dto.UserRegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    String getClientIP(HttpServletRequest request);
    String register(UserRegisterDto userRegisterDto);
    ResponseEntity<JWTTokenDto> login(LoginDto loginDto, HttpServletRequest request);

}
