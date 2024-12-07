package cc.hrva.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JWTTokenDto {

    private String token;
    private UserDto user;

}
