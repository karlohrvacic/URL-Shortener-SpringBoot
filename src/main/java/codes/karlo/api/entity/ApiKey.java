package codes.karlo.api.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String key;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "apiKey")
    private List<Url> urls;

    private Long apiCallsLimit;

    private Long apiCallsUsed;

    private LocalDateTime createDate;

    private LocalDateTime expirationDate;

    @PrePersist
    public void onCreate() {
        this.createDate = LocalDateTime.now();
        this.apiCallsUsed = 0L;
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

    @Override
    public String toString() {
        return "ApiKey{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", owner=" + owner.getId() +
                ", urls size=" + urls.size() +
                ", apiCallsLimit=" + apiCallsLimit +
                ", apiCallsUsed=" + apiCallsUsed +
                ", createDate=" + createDate +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
