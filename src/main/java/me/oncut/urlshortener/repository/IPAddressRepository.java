package me.oncut.urlshortener.repository;

import java.util.List;
import me.oncut.urlshortener.model.IPAddress;
import me.oncut.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPAddressRepository extends JpaRepository<IPAddress, Long> {

    List<IPAddress> findByUrlAndActiveTrue(Url url);

    List<IPAddress> findByActiveTrue();

    List<IPAddress> findByActiveFalse();

}
