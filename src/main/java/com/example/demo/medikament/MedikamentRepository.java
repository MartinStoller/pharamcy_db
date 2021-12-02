package com.example.demo.medikament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedikamentRepository extends JpaRepository<Medikament, Long>{
/*    This represents the Data-access layer (The annotation takes care of everything)*/
}
