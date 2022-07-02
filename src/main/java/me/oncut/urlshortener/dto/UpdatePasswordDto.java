package me.oncut.urlshortener.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdatePasswordDto {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

}
