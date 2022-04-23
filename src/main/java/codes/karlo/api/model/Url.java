package codes.karlo.api.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;

@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Url {

    @Id
    @SequenceGenerator(name = "url_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "url_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "You need to add url for shortening")
    @URL(message = "Long URL not valid")
    private String longUrl;

    @Column(unique = true)
    private String shortUrl;

    @ManyToOne
    private User owner;

    @ManyToOne
    private ApiKey apiKey;

    private LocalDateTime createDate;

    private LocalDateTime lastAccessed;

    private Long visits;

    private Long visitLimit;

    private boolean isActive;

    @PrePersist
    public void onCreate() {
        this.visits = 0L;
        this.createDate = LocalDateTime.now();
        this.isActive = true;
    }

    public Url onVisit() {
        this.visits++;
        this.lastAccessed = LocalDateTime.now();
        if (this.visits >= Optional.ofNullable(visitLimit).orElse(this.visits + 1)) {
            isActive = false;
        }
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final Url url = (Url) o;
        return id != null && Objects.equals(id, url.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", longUrl='" + longUrl + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", owner id=" + owner.getId() +
                ", apiKey id=" + apiKey.getId() +
                ", createDate=" + createDate +
                ", lastAccessed=" + lastAccessed +
                ", visits=" + visits +
                '}';
    }
}
