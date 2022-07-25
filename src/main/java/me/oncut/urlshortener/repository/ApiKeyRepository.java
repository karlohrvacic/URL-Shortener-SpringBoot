package me.oncut.urlshortener.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import me.oncut.urlshortener.model.ApiKey;
import me.oncut.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    Optional<ApiKey> findApiKeyByKey(String key);

    List<ApiKey> findByExpirationDateIsLessThanEqualAndActiveTrue(LocalDateTime createDate);

    List<ApiKey> findByOwnerAndActiveTrue(User owner);

}
