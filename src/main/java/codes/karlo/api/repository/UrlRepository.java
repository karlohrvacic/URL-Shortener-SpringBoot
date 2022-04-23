package codes.karlo.api.repository;

import codes.karlo.api.model.Url;
import codes.karlo.api.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortUrl(String shortUrl);

    @Query(value = "select * " +
            "from Url u " +
            "where u.long_url = ?1 and u.is_active = true " +
            "order by u.create_date asc " +
            "limit 1", nativeQuery = true)
    Optional<Url> findByLongUrlAndActiveIsTrue(String longUrl);

    @Query(value = "select * " +
            "from Url u " +
            "where u.short_url = ?1 and u.is_active = true " +
            "order by u.create_date asc " +
            "limit 1", nativeQuery = true)
    Optional<Url> findByShortUrlAndActiveIsTrue(String longUrl);

    Optional<List<Url>> findAllByOwner(User owner);

    boolean existsUrlByLongUrl(String longUrl);

    boolean existsUrlByShortUrl(String shortUrl);

}
