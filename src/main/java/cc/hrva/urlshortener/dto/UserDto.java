package cc.hrva.urlshortener.dto;

import cc.hrva.urlshortener.model.codebook.Authorities;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private Long apiKeySlots;

    private List<Authorities> authorities;

    private LocalDateTime createDate;

    private LocalDateTime lastLogin;

}
