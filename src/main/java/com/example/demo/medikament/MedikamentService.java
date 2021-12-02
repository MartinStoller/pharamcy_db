package com.example.demo.medikament;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedikamentService {
/*    Responsible for business logic*/
    public static List<Medikament> getMedikamente() {
/*        TODO: return all objects in database*/
        return List.of(new Medikament(
                10012233,
                "SuperMedikament5000",
                "Wirkstoff XY",
                "Bayer",
                45));
    }
}
