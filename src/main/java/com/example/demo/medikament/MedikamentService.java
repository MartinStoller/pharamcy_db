package com.example.demo.medikament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedikamentService {
/*    Responsible for business logic*/

/*    Get Repository for data access:*/
    private final MedikamentRepository medikamentRepository;

    @Autowired //Dependency injection (makes sure the Service gets passed an instance of Repository - without that
    //annotation we would have to write this.medRepository = new medRepository, which would work as well but is unconventional
    public MedikamentService(MedikamentRepository medikamentRepository) {
        this.medikamentRepository = medikamentRepository;
    }

    public List<Medikament> getMedikamente() {
/*        return all objects in database*/
        return medikamentRepository.findAll();
    }
}
