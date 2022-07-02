package me.oncut.urlshortener.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PasswordResetDto {

    @NotBlank
    private String token;

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
