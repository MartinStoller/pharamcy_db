package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/bestellung")
@Validated
public class BestellungController {

    private final BestellungService bestellungService;

    @Autowired
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
    public void createNewOrder(@Valid @RequestBody Bestellung bestellung){
        bestellungService.addNewOrder(bestellung);
    }

    @DeleteMapping(path="/del/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) throws InstanceNotFoundException {
        bestellungService.deleteOrder(id);
        return ResponseEntity.ok("Deletion of " + id + " successful.");
    }

    @PutMapping(path="/status/{id}/{new}")
    @ResponseStatus(value = HttpStatus.OK, reason = "Status changed successfully")
    // Note, that when we set reason, Spring calls HttpServletResponse.sendError(). Therefore, it will send an HTML error page to the client,
    // which makes it a bad fit for REST endpoints. I only used this here for demonstration purposes.
    public void changeStatus(@PathVariable("id") Long id, @PathVariable("new") @Min(0) @Max(4) int new_status){
        bestellungService.changeStatus(id, new_status);
    }
}
