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
public class ApiKeyUpdateDto {

    @NotNull(message = "ID of API key can't be null!")
    private Long id;
    private Long apiCallsLimit;
    private Long apiCallsUsed;
    private LocalDateTime expirationDate;
}