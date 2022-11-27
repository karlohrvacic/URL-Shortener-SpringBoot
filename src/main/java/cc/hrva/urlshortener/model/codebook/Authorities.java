package cc.hrva.urlshortener.model.codebook;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Builder
@Getter
@Setter
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
