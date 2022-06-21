package me.oncut.urlshortener.configuration;

import lombok.RequiredArgsConstructor;
import me.oncut.urlshortener.configuration.properties.AppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AppProperties.class)
public class AppConfiguration {

}
