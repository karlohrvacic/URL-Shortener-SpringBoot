package codes.karlo.api.converter;

import codes.karlo.api.dto.UserUpdateDto;
import codes.karlo.api.model.User;
import codes.karlo.api.exception.UserDoesntExistException;
import codes.karlo.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateDtoToUserConverter implements Converter<UserUpdateDto, User> {

    private final UserRepository userRepository;

    @Override
    public User convert(final UserUpdateDto userUpdateDto) {
        final User existingUser = userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new UserDoesntExistException(String.format("User with ID %d has not been found",
                        userUpdateDto.getId())));
        existingUser.setName(userUpdateDto.getName());
        existingUser.setEmail(userUpdateDto.getEmail());
        return existingUser;
    }

}
