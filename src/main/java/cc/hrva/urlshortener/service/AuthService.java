package cc.hrva.urlshortener.service;

import javax.servlet.http.HttpServletRequest;
import cc.hrva.urlshortener.dto.JWTTokenDto;
import cc.hrva.urlshortener.dto.LoginDto;
import cc.hrva.urlshortener.dto.UserRegisterDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    String register(UserRegisterDto userRegisterDto);

    ResponseEntity<JWTTokenDto> login(LoginDto loginDto, HttpServletRequest request);

    String getClientIP(HttpServletRequest request);

}
