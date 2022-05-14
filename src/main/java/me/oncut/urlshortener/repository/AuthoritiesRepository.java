package me.oncut.urlshortener.repository;

import me.oncut.urlshortener.model.codebook.Authorities;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {

    Optional<Authorities> findByName(String name);
}
