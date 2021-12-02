package com.example.demo.medikament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/medikament")
public class MedikamentController {
/*    Class, which contains all the resources for our API -> functions the user can call*/

    private final MedikamentService medikamentService;

    @Autowired //Dependency injection (makes sure a Service instance gets passed to Controller - without that
    //annotation we would have to write this.medService = new medService, which would work as well but is unconventional
    public MedikamentController(MedikamentService medikamentService) {
        this.medikamentService = medikamentService;
    }

    @GetMapping
    public List<Medikament> getMedikamente() {
        return medikamentService.getMedikamente();
    }

}
