package codes.karlo.api.repository;

import codes.karlo.api.entity.Authorities;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {

    Optional<Authorities> findByName(String name);
}
