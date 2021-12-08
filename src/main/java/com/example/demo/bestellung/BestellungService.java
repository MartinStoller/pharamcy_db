package com.example.demo.bestellung;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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

    public Optional<Bestellung> getSpecificBestellung(Long id) {
        return bestellungRepository.findBestellungdById(id);
    }

    public void addNewOrder(Bestellung bestellung){
        Optional<Bestellung> newOrder = bestellungRepository.findBestellungdById(bestellung.getId());
        if (newOrder.isPresent()) {
            throw new IllegalStateException("This Order is already in the database!");
        }
        bestellungRepository.save(bestellung);
    }

    public void deleteOrder(Long id){
        boolean exists = bestellungRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Order with id " + id + " does not exist!");
        }
        bestellungRepository.deleteById(id);
    }

    @Transactional
    public void changeStatus(Long id, int new_status){
        boolean exists = bestellungRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Order with id " + id + " does not exist!");
        }
        Bestellung order = bestellungRepository.findById(id).orElse(null);/*
        int[] allowedStatus = {0, 1, 2, 3, 4};
        boolean contains = IntStream.of(allowedStatus).anyMatch(x -> x==new_status);
        if (!contains) {
            throw new IllegalStateException("Illegal value: " + new_status + " is not a valid status!");
        }*/
        order.setStatus(new_status);
    }
}
