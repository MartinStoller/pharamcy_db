package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BestellungRepository extends JpaRepository<Bestellung, Long> {

    @Query("SELECT b FROM Bestellung b WHERE b.id = ?1")
    Optional<Bestellung> findBestellungdById(Long id);
}

