package me.oncut.urlshortener.converter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.configuration.properties.AppProperties;
import me.oncut.urlshortener.dto.UserRegisterDto;
import me.oncut.urlshortener.model.User;
import me.oncut.urlshortener.service.AuthoritiesService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterDtoToUserConverter implements Converter<UserRegisterDto, User> {

    private final AuthoritiesService authoritiesService;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    @Override
    public User convert(final UserRegisterDto userRegisterDto) {
        return User.builder()
                .name(userRegisterDto.getName())
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .authorities(List.of(authoritiesService.getDefaultAuthority()))
                .apiKeySlots(appProperties.getUserApiKeySlots())
                .build();
    }

}
