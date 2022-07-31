package me.oncut.urlshortener.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;
    private String name;
    private String email;
    private Long apiKeySlots;
    private Boolean active;
}
