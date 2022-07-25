package me.oncut.urlshortener.model;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
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
public class ResetToken {

    @Id
    @SequenceGenerator(name = "reset_token_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "reset_token_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private User user;

    private String token;

    private LocalDateTime createDate;

    private LocalDateTime expirationDate;

    private boolean active;

    @PrePersist
    public void onCreate() {
        this.token = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
        this.active = true;
    }

}
