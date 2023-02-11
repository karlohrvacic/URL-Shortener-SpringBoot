package cc.hrva.urlshortener.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties("app.google-api")
public class GoogleApiProperties {

    private String key;
    private String clientId;
    private String clientVersion;

}
