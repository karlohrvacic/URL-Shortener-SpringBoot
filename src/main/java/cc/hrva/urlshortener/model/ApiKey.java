package cc.hrva.urlshortener.model;

import cc.hrva.urlshortener.configuration.properties.AppProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiKey {

    @Id
    @SequenceGenerator(name = "api_key_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "api_key_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String key;

    @ManyToOne
    private User owner;

    @JsonIgnore
    @OneToMany(mappedBy = "apiKey")
    private List<Url> urls;

    private Long apiCallsLimit;

    private Long apiCallsUsed;

    private LocalDateTime createDate;

    private LocalDateTime expirationDate;

    private boolean active;

    public ApiKey(final User user, final AppProperties appProperties) {
        this.key = UUID.randomUUID().toString();
        this.owner = user;
        this.apiCallsLimit = appProperties.getApiKeyCallsLimit();
        this.expirationDate = LocalDateTime.now().plusMonths(appProperties.getApiKeyExpirationInMonths());
    }

    @PrePersist
    public void onCreate() {
        this.createDate = LocalDateTime.now();
        this.apiCallsUsed = 0L;
        this.active = true;
    }

    public ApiKey apiKeyUsed() {
        this.apiCallsUsed++;
        verifyApiKeyValidity();

        return this;
    }

    private void verifyApiKeyValidity() {
        if (this.apiCallsUsed >= Optional.ofNullable(this.apiCallsLimit).orElse(this.apiCallsUsed + 1) ||
                Optional.ofNullable(this.expirationDate).orElse(LocalDateTime.now().plusDays(1)).isBefore(LocalDateTime.now())) {
            this.active = false;
        }
    }

    public ApiKey deactivate() {
        this.active = false;

        return this;
    }

}
