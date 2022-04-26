package codes.karlo.api.repository;

import codes.karlo.api.model.Url;
import codes.karlo.api.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortUrl(String shortUrl);

    Optional<Url> findByLongUrlAndIsActiveTrue(String longUrl);

    Optional<Url> findByShortUrlAndIsActiveTrue(String shortUrl);

    Optional<List<Url>> findAllByOwner(User owner);

    boolean existsUrlByLongUrlAndIsActiveTrue(String longUrl);

    boolean existsUrlByShortUrl(String shortUrl);

}
