package cc.hrva.urlshortener.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdatePasswordDto {

    @NotBlank(message = "Old password is required")
    private String oldPassword;

  @NotBlank(message = "New password is required")
    private String newPassword;

}
