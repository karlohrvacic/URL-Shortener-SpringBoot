package me.oncut.urlshortener.repository;

import me.oncut.urlshortener.model.ApiKey;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    Optional<ApiKey> findApiKeyByKey(String key);

}
