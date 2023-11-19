package cc.hrva.urlshortener.model.codebook;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authorities implements GrantedAuthority {

    @Id
    private Long id;

    private String name;

    private Boolean active;

    @Override
    public String getAuthority() {
        return getName();
    }

}
