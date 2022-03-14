package codes.karlo.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class ApiKeyUpdateDto {

    @NotNull
    private Long id;

    private Long apiCallsLimit;

    private Long apiCallsUsed;

    private LocalDateTime expirationDate;
}