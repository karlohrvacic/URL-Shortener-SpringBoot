package codes.karlo.api.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiKeyUpdateDto {

    @NotNull
    private Long id;

    private Long apiCallsLimit;

    private Long apiCallsUsed;

    private LocalDateTime expirationDate;
}