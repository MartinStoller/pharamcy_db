package com.example.demo.bestellung;

import com.example.demo.medikament.Medikament;
import com.example.demo.medikament.MedikamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/bestellung")
public class BestellungController {

    private final BestellungService bestellungService;

    @Autowired
    public BestellungController(BestellungService bestellungService) {
        this.bestellungService = bestellungService;
    }

    @GetMapping
    public List<Bestellung> getBestellung() {
        return bestellungService.getBestellungen();
    }
}
