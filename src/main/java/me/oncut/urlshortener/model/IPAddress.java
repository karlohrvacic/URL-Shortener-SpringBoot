package me.oncut.urlshortener.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
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
