package me.oncut.urlshortener.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UrlUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    private Long visitLimit;

    private LocalDateTime expirationDate;

}
