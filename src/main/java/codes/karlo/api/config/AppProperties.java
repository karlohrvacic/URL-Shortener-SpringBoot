package codes.karlo.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties("app")
public class AppProperties {
    private String frontendUrl;
    private int apiKeyLength;
    private int urlShortLength;
    private int jwtTokenValiditySeconds;
    private String jwtBase64Secret;
    private Long apiKeyCallsLimit;
}
