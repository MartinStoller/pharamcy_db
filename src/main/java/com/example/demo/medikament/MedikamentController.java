package com.example.demo.medikament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
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

    @GetMapping(path="/all")
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

    @PostMapping
    public ResponseEntity<String> registerNewMed(@Valid @RequestBody Medikament medikament) { //@Valid valdidates the requestbody based on the constraints, which we have set
        // If the validation fails, it will trigger a MethodArgumentNotValidException. By default, Spring will translate this exception to a HTTP status 400 (Bad Request).
        // We take the Response Body as input(Annotation) and map it into a Med (Datatype Medikament), which
        // then can get added to the db by the method
        medikamentService.addNewMed(medikament);
        return ResponseEntity.ok("valid");
    }
    @GetMapping(path="/allAvailable")
    @ResponseBody
    @ResponseStatus
    public List<Medikament> getAvailableMeds() {
    return medikamentService.getAvailableMeds();
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<String> deleteMed(@PathVariable("id") Long id){
        medikamentService.deleteMed(id);
        return ResponseEntity.ok("valid");
    }

    @PutMapping(path="/reduceStockAfterOrder/{id}/{ordervolume}") // I could also do this with a RequestParam such as below
    public void reduceVorratAfterOrder(@PathVariable("id") Long id, @PathVariable("ordervolume") int ordervolume){
        medikamentService.reduceVorratAfterOrder(id, ordervolume);
    }

    @PutMapping(path="/increase/{id}") // RequestParam is the alternative to multiple PathVariables
    public void increaseVorrat(@PathVariable("id") Long id, @RequestParam int extra){
        medikamentService.increaseVorrat(id, extra);
    }

}
