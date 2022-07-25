package me.oncut.urlshortener.model;

import java.time.LocalDateTime;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

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
        verifyUrlValidity();
        return this;
    }

    public void verifyUrlValidity() {
        if (this.visits >= Optional.ofNullable(visitLimit).orElse(this.visits + 1)) {
            active = false;
        }
    }

    public void clearForAnonymousUser() {
        this.owner = null;
        this.apiKey = null;
        this.visitLimit = null;
    }

}
