package codes.karlo.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "frontend")
public class FrontendProperties {

    /**
     * The url of frontend.
     */
    String url;

}
