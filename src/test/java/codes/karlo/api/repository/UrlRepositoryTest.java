package codes.karlo.api.repository;

import codes.karlo.api.entity.Url;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Data
class UrlRepositoryTest {

    private UrlRepository urlRepository;

    private TestEntityManager entityManager;

    @Autowired
    public UrlRepositoryTest(UrlRepository urlRepository, TestEntityManager entityManager) {
        this.urlRepository = urlRepository;
        this.entityManager = entityManager;
    }

    @BeforeEach
    void setUp() {
        Url url = Url.builder()
                .longUrl("https://karlo.codes")
                .shortUrl("karlo")
                .visits(0L)
                .createDate(LocalDateTime.now())
                .build();

        entityManager.persist(url);
    }

    @Test
    public void findByShortUrl_returnUrl() {
        Url url = urlRepository.findByShortUrl("karlo").get();
        assertEquals(url.getLongUrl(), "https://karlo.codes");
    }
}