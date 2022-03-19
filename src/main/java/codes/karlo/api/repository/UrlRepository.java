package codes.karlo.api.repository;

import codes.karlo.api.entity.Url;
import codes.karlo.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortUrl(String shortUrl);

    @Query(value = "select * " +
            "from Url u " +
            "where u.long_url = ?1 and u.is_active = true " +
            "order by u.create_date asc " +
            "limit 1", nativeQuery = true)
    Optional<Url> findByLongUrlAndActiveIsTrue(String longUrl);

    Optional<List<Url>> findAllByOwner(User owner);

    boolean existsUrlByLongUrl(String longUrl);

    boolean existsUrlByShortUrl(String shortUrl);

}
