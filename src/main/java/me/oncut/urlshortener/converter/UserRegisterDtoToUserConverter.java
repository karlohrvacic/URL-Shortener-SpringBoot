package me.oncut.urlshortener.converter;

import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.dto.UserRegisterDto;
import me.oncut.urlshortener.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterDtoToUserConverter implements Converter<UserRegisterDto, User> {

    @Override
    public User convert(final UserRegisterDto userRegisterDto) {
        return User.builder()
                .name(userRegisterDto.getName())
                .password(userRegisterDto.getPassword())
                .email(userRegisterDto.getEmail())
                .build();
    }

}
