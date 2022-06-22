package me.oncut.urlshortener.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlUpdateDto {

    @NotNull
    private Long id;

    private Long visitLimit;

    private LocalDateTime expirationDate;

}
