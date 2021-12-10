package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedikamentRepository extends JpaRepository<Medikament, Long>{
/*    This represents the Data-access layer (The annotation takes care of everything)*/
    @Query("SELECT m FROM Medikament m WHERE m.vorrat > 0")//below method will execute this JPQL command
    List<Medikament> findAvailableMeds();

}
