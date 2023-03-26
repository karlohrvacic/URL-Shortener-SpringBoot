package cc.hrva.urlshortener.beans;

import cc.hrva.urlshortener.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HttpMessageConverter<String> messageConverter;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final AuthenticationException e) throws IOException {
        final var apiError = new ApiException(UNAUTHORIZED);
        apiError.setMessage(e.getMessage());
        apiError.setDebugMessage(e.getMessage());

        try (final ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse)) {
            outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);

            messageConverter.write(objectMapper.writeValueAsString(apiError), MediaType.APPLICATION_JSON, outputMessage);
        }
    }

}
