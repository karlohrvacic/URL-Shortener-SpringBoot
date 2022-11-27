package cc.hrva.urlshortener.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ip_address")
public class IPAddress {

    @Id
    @SequenceGenerator(name = "ip_address_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "ip_address_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "hashed_ip_address")
    private String hashedIPAddress;

    @ManyToOne
    private Url url;

    private Long visits;

    private LocalDateTime createDate;

    private boolean active;

    @PrePersist
    public void onCreate() {
        this.createDate = LocalDateTime.now();
        this.visits = 1L;
        this.active = true;
    }

    public IPAddress addVisit() {
        this.visits++;
        return this;
    }

}
