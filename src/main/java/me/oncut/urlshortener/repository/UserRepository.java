package me.oncut.urlshortener.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import me.oncut.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByLastLoginIsLessThanEqualAndActiveTrue(LocalDateTime limitDate);

}
