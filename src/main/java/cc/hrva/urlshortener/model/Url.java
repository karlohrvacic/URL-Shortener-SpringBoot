package cc.hrva.urlshortener.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.cache.annotation.CacheEvict;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Url {

    @Id
    @SequenceGenerator(name = "url_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "url_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @URL(message = "Long URL not valid")
    @NotBlank(message = "You need to add url for shortening")
    private String longUrl;

    @Column(unique = true)
    private String shortUrl;

    @ManyToOne
    private User owner;

    @ManyToOne
    private ApiKey apiKey;

    private LocalDateTime createDate;

    private LocalDateTime lastAccessed;

    private LocalDateTime expirationDate;

    private Long visits;

    private Long visitLimit;

    private boolean active;

    @PrePersist
    public void onCreate() {
        this.visits = 0L;
        this.createDate = LocalDateTime.now();
        this.active = true;
    }

    public Url onVisit() {
        this.visits++;
        this.lastAccessed = LocalDateTime.now();
        verifyUrlValidity(this);
        return this;
    }

    @CacheEvict(value = "urls", key = "#url.shortUrl", condition = "#url.active == false")
    public void verifyUrlValidity(final Url url) {
        if (this.visits >= Optional.ofNullable(visitLimit).orElse(this.visits + 1)) {
            this.active = false;
        }
    }

    public void clearForAnonymousUser() {
        this.owner = null;
        this.apiKey = null;
    }

}
