package codes.karlo.api.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UrlServiceTest {

//    @Autowired
//    private UrlService urlService;
//
//    @MockBean
//    private UrlRepository urlRepository;
//
//    @BeforeEach
//    void setUp() {
//        Url url = Url.builder()
//                .longUrl("https://karlo.codes")
//                .shortUrl("karlo")
//                .visits(0L)
//                .createDate(LocalDateTime.now())
//                .build();
//
//        Mockito.when(urlRepository.findByShortUrl("karlo"))
//                .thenReturn(Optional.of(url));
//    }
//
//    @Test
//    @DisplayName("Get URL from valid short url")
//    public void findByShortUrl_ifFoundReturn() throws UrlNotFoundException {
//        String shortUrl = "karlo";
//        Url found = urlService.fetchUrlByShortUrl(shortUrl);
//
//        assertEquals(shortUrl, found.getShortUrl());
//    }
}