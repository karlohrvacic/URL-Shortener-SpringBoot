package cc.hrva.urlshortener.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiKeyUpdateDto {

    @NotNull(message = "ID of API key can't be null!")
    private Long id;
    private Long apiCallsLimit;
    private Long apiCallsUsed;
    private LocalDateTime expirationDate;
    private Boolean active;

}