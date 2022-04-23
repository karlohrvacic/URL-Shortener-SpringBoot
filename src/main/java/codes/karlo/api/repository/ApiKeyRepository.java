package codes.karlo.api.repository;

import codes.karlo.api.model.ApiKey;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    Optional<ApiKey> findApiKeyByKey(String key);

}
