package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    public Bestellung getSpecificBestellung(Long id) throws InstanceNotFoundException{
        Optional<Bestellung> bestellungOptional =  bestellungRepository.findById(id);
        //check if optional is empty, if yes throw, ObjectNotFoundException
        if (bestellungOptional.isPresent()) {
            System.out.println(bestellungOptional);
            return bestellungOptional.get();
        }
        throw new InstanceNotFoundException(bestellungOptional.toString());

    }

    public void addNewOrder(Bestellung bestellung){
        Optional<Bestellung> newBestellung = bestellungRepository.findById(bestellung.getId());
        if (newBestellung.isPresent()) {
            throw new IllegalStateException("This Order is already in the database!");
        }
        bestellungRepository.save(bestellung);
    }

    public void deleteOrder(Long id) throws InstanceNotFoundException{
        boolean exists = bestellungRepository.existsById(id);
        if (!exists) {
            throw new InstanceNotFoundException("Order with id " + id + " does not exist!");
        }
        bestellungRepository.deleteById(id);
    }

    @Transactional
    public void changeStatus(Long id, int new_status){
        boolean exists = bestellungRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Order with id " + id + " does not exist!");
        }
        Bestellung order = bestellungRepository.findById(id).orElse(null);
        order.setStatus(new_status);
    }
}
