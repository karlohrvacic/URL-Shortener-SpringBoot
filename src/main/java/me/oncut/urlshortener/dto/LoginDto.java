package me.oncut.urlshortener.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginDto {

    @NotBlank(message = "Email is required")
    private String email;

  @NotBlank(message = "Password is required")
    private String password;
}
