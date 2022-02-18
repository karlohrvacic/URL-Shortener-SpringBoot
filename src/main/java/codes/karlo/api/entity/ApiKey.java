package codes.karlo.api.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @Column(unique = true)
    private String apiKey;

    private Long apiCallsLimit;

    private LocalDateTime createDate;

    private LocalDateTime expirationDate;

    @PrePersist
    public void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApiKey apiKey = (ApiKey) o;
        return id != null && Objects.equals(id, apiKey.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
