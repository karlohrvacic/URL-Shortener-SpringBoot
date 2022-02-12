package codes.karlo.api.entitiy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String longUrl;
    private String shortUrl;
    private LocalDateTime createDate;
    private LocalDateTime lastAccessed;
    private Long visits;

    public Url(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.visits = 0L;
        this.createDate = LocalDateTime.now();
    }

    public Url() {

    }

    @PostLoad
    private void onVisit() {
        this.visits++;
        this.lastAccessed = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }

}
