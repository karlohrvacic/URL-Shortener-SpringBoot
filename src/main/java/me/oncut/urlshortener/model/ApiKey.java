package me.oncut.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
