package me.oncut.urlshortener.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDto {

    @NotNull
    private String token;

    @NotNull
    private LoginDto loginDto;
}
