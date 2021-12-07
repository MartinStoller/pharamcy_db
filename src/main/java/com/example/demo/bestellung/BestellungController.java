package com.example.demo.bestellung;

import com.example.demo.medikament.Medikament;
import com.example.demo.medikament.MedikamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/bestellung")
public class BestellungController {

    private final BestellungService bestellungService;

    @Autowired
    public BestellungController(BestellungService bestellungService) {
        this.bestellungService = bestellungService;
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus
    public List<Bestellung> getBestellungen() {
        return bestellungService.getBestellungen();
    }

    @GetMapping(path="/get/{id}")
    @ResponseBody
    @ResponseStatus
    public Optional<Bestellung> getSpecificBestellung(@PathVariable("id") Long id) {
        return bestellungService.getSpecificBestellung(id);
    }

    @PostMapping(path="addnew")
    public void createNewOrder(@RequestBody Bestellung bestellung){
        bestellungService.addNewOrder(bestellung);
    }

    @DeleteMapping(path="/del/{id}")
    public void deleteOrder(@PathVariable("id") Long id){
        bestellungService.deleteOrder(id);
    }

    @PutMapping(path="/setstatus/{id}/{new}")
    public void changeStatus(@PathVariable("id") Long id, @PathVariable("new") int new_status){
        bestellungService.changeStatus(id, new_status);
    }
}
