package me.oncut.urlshortener.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import me.oncut.urlshortener.model.ResetToken;
import me.oncut.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    Optional<ResetToken> findResetTokenByUserAndTokenAndActiveTrue(User user, String token);

    List<ResetToken> findByActiveTrue();

    List<ResetToken> findByUserAndActiveTrue(User user);

    List<ResetToken> findByExpirationDateIsLessThanEqualAndActiveTrue(LocalDateTime expirationDate);

    List<ResetToken> findByExpirationDateIsLessThanEqualAndActiveFalse(LocalDateTime expirationDate);

}
