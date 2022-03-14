package codes.karlo.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginDto {

    @NotNull
    private String email;

    @NotNull
    private String password;
}
