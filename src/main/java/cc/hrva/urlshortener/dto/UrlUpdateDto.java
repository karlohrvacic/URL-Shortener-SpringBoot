package cc.hrva.urlshortener.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
