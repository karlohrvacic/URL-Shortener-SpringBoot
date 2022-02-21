package codes.karlo.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "You need to add url for shortening")
    @URL(message = "Long URL not valid")
    @Column(unique = true)
    private String longUrl;

    @Column(unique = true)
    private String shortUrl;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private User owner;

    private LocalDateTime createDate;

    private LocalDateTime lastAccessed;

    private Long visits;

    public Url(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    @PrePersist
    public void onCreate() {
        this.visits = 0L;
        this.createDate = LocalDateTime.now();
    }

    public Url onVisit() {
        this.visits++;
        this.lastAccessed = LocalDateTime.now();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Url url = (Url) o;
        return id != null && Objects.equals(id, url.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
