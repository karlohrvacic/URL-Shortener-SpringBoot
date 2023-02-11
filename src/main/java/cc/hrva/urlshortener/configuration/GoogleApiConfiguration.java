package cc.hrva.urlshortener.configuration;

import cc.hrva.urlshortener.configuration.properties.GoogleApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GoogleApiProperties.class)
public class GoogleApiConfiguration {
}
