package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path="/bestellung") //should be plural
@Validated
public class BestellungController {

    private final BestellungService bestellungService;

    public BestellungController(BestellungService bestellungService) {
        this.bestellungService = bestellungService;
    }

    @GetMapping(path="/all")
    public List<Bestellung> getBestellungen() {
        return bestellungService.getBestellungen();
    }

    @GetMapping(path="/{id}")
    public Bestellung getSpecificBestellung(@PathVariable("id") Long id) throws InstanceNotFoundException {
        return bestellungService.getSpecificBestellung(id);
    }

    @PostMapping
    public void createNewOrder(@Valid @RequestBody Bestellung bestellung) throws InstanceAlreadyExistsException {
        bestellungService.addNewOrder(bestellung);
    }

    @DeleteMapping(path="/del/{id}") //a restful approach would be to just use the id as path to get a full path of /bestellungen{id} | /orders/{id}. The /del path element does not give use more information
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) throws InstanceNotFoundException {
        bestellungService.deleteExistingById(id);
        return ResponseEntity.ok("Deletion of " + id + " successful.");
    }

    @PutMapping(path="/status/{id}/{new}")
    @ResponseStatus(value = HttpStatus.OK)
    public void changeStatus(@PathVariable("id") Long id, @PathVariable("new") @Min(0) @Max(3) int new_status){
        //actually i should make "new" a request parameter... passing it as pathvariable is bad practice
        bestellungService.changeStatus(id, new_status);
    }
}
