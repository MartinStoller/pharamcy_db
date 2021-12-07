package com.example.demo.medikament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/medikament")
public class MedikamentController {
/*    Class, which contains all the resources for our API -> functions the user can call*/

    private final MedikamentService medikamentService;

    @Autowired //Dependency injection (makes sure a Service instance gets passed to Controller - without that
    //annotation we would have to write this.medService = new medService, which would work as well but is unconventional
    public MedikamentController(MedikamentService medikamentService) {
        this.medikamentService = medikamentService;
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus
    public List<Medikament> getMedikamente() {
        return medikamentService.getMedikamente();
    }

    @GetMapping(path="/{id}")
    @ResponseBody
    @ResponseStatus
    public Optional<Medikament> getSpecificMed(@PathVariable("id") Long id) {
        return medikamentService.getSpecificMed(id);
    }

    @PostMapping(path="/addnew")
    public void registerNewMed(@RequestBody Medikament medikament) {
        // We take the Response Body as input(Annotation) and map it into a Med (Datatype Medikament), which
        // then can get added to the db by the method
        medikamentService.addNewMed(medikament);
    }
    @GetMapping(path="/allavailable")
    @ResponseBody
    @ResponseStatus
    public List<Medikament> getAvailableMeds() {
    return medikamentService.getAvailableMeds();
    }

    @DeleteMapping(path="/{id}")
    public void deleteMed(@PathVariable("id") Long id){
        medikamentService.deleteMed(id);
    }

    @PutMapping(path="/reducestockafterorder/{id}/{ordervolume}")
    public void reduceVorratAfterOrder(@PathVariable("id") Long id, @PathVariable("ordervolume") int ordervolume){
        medikamentService.reduceVorratAfterOrder(id, ordervolume);
    }

    @PutMapping(path="/increase/{id}/{extra}")
    public void increaseVorrat(@PathVariable("id") Long id, @PathVariable("extra") int extra){
        medikamentService.increaseVorrat(id, extra);
    }

}
