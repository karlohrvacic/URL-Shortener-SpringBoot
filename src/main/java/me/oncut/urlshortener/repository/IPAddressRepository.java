package me.oncut.urlshortener.repository;

import java.time.LocalDateTime;
import java.util.List;
import me.oncut.urlshortener.model.IPAddress;
import me.oncut.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPAddressRepository extends JpaRepository<IPAddress, Long> {

    List<IPAddress> findByUrlAndActiveTrue(Url url);

    List<IPAddress> findByCreateDateIsLessThanEqualAndActiveTrue(LocalDateTime createDate);

    List<IPAddress> findByCreateDateIsLessThanEqualAndActiveFalse(LocalDateTime createDate);

}
