package cc.hrva.urlshortener;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@OpenAPIDefinition
@EnableWebSecurity
@SpringBootApplication
public class UrlShortenerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }

}
