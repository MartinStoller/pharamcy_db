package com.example.demo.bestellung;

import com.example.demo.medikament.Medikament;
import com.example.demo.medikament.MedikamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BestellungService {

    private final BestellungRepository bestellungRepository;

    @Autowired
    public BestellungService(BestellungRepository bestellungRepository) {
        this.bestellungRepository = bestellungRepository;
    }

    public List<Bestellung> getBestellungen() {
        /*        return all objects in database*/
        return bestellungRepository.findAll();
    }
}
