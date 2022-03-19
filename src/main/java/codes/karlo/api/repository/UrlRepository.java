package codes.karlo.api.repository;

import codes.karlo.api.entity.Url;
import codes.karlo.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortUrl(String shortUrl);

    Optional<Url> findByLongUrl(String longUrl);

    Optional<List<Url>> findAllByOwner(User owner);

    boolean existsUrlByLongUrl(String longUrl);

}
