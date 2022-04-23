package codes.karlo.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties("app")
public class AppProperties {
    private String frontendUrl;
    private String jwtBase64Secret;
    private Long apiKeyLength;
    private Long shortUrlLength;
    private Long jwtTokenValiditySeconds;
    private Long apiKeyCallsLimit;
    private Long apiKeyExpirationInMonths;
}
