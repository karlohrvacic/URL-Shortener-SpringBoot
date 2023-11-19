package cc.hrva.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateUrlDto {

  @NotBlank(message = "Long URL is required")
  private String longUrl;
  private String shortUrl;
  private Long visitLimit;
  private LocalDateTime expirationDate;

}
