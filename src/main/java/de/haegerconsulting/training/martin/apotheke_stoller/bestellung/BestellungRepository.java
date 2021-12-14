package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BestellungRepository extends JpaRepository<Bestellung, Long> {

}

