package codes.karlo.api.service;

import codes.karlo.api.entity.Url;
import codes.karlo.api.exception.UrlNotFoundException;
import codes.karlo.api.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UrlServiceTest {

    @Autowired
    private UrlService urlService;

    @MockBean
    private UrlRepository urlRepository;

    @BeforeEach
    void setUp() {
        Url url = Url.builder()
                .longUrl("https://karlo.codes")
                .shortUrl("karlo")
                .visits(0L)
                .createDate(LocalDateTime.now())
                .build();

        Mockito.when(urlRepository.findByShortUrl("karlo"))
                .thenReturn(Optional.of(url));
    }

    @Test
    @DisplayName("Get URL from valid short url")
    public void findByShortUrl_ifFoundReturn() throws UrlNotFoundException {
        String shortUrl = "karlo";
        Url found = urlService.fetchUrlByShortUrl(shortUrl);

        assertEquals(shortUrl, found.getShortUrl());
    }
}