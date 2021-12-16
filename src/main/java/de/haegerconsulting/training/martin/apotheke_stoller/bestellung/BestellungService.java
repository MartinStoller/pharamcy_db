package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BestellungService {

    private final BestellungRepository bestellungRepository;

    public BestellungService(BestellungRepository bestellungRepository) {
        this.bestellungRepository = bestellungRepository;
    }

    public List<Bestellung> getBestellungen() {
        /*        return all objects in database*/
        return bestellungRepository.findAll();
    }

    public Bestellung getSpecificBestellung(Long id) throws InstanceNotFoundException{
        return bestellungRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException("Could not find Order with id " + id));
    }

    public void addNewOrder(Bestellung bestellung) throws InstanceAlreadyExistsException {
        // since we're not expecting an id while creating a new order, checking if this order does already exist is not really necessary
        Optional<Bestellung> newBestellung = bestellungRepository.findById(bestellung.getId());
        if (newBestellung.isPresent()) {
            throw new InstanceAlreadyExistsException("This Order is already in the database!");
        }
        bestellungRepository.save(bestellung);
    }

    public void deleteExistingById(Long id) throws InstanceNotFoundException{
        boolean exists = bestellungRepository.existsById(id);
        if (!exists) {
            throw new InstanceNotFoundException("Order with id " + id + " does not exist!");
        }
        bestellungRepository.deleteById(id);
    }

    @Transactional
    public void changeStatus(Long id, BestellungStatus new_status){
        boolean exists = bestellungRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Order with id " + id + " does not exist!"); //Nullpointerexception would be better
        }
        Bestellung order = bestellungRepository.findById(id).orElse(null);
        order.setStatus(new_status);
    }
}
