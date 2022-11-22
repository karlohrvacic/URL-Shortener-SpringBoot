package cc.hrva.urlshortener.repository;

import lombok.Data;

@Data
class UrlRepositoryTest {

//    private UrlRepository urlRepository;
//
//    private TestEntityManager entityManager;
//
//    @Autowired
//    public UrlRepositoryTest(UrlRepository urlRepository, TestEntityManager entityManager) {
//        this.urlRepository = urlRepository;
//        this.entityManager = entityManager;
//    }
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
//        entityManager.persist(url);
//    }
//
//    @Test
//    public void findByShortUrl_returnUrl() {
//        Url url = urlRepository.findByShortUrlAndActive("karlo").get();
//        assertEquals(url.getLongUrl(), "https://karlo.codes");
//    }
}