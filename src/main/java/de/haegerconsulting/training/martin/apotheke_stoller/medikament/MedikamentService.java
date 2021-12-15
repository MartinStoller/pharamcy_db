package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.naming.LimitExceededException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MedikamentService {
    /*    Responsible for business logic*/

    /*    Get Repository for data access:*/
    private final MedikamentRepository medikamentRepository;

    public MedikamentService(MedikamentRepository medikamentRepository) {
        this.medikamentRepository = medikamentRepository;
    }

    public List<Medikament> getMedikamente() {
        /*        return all objects in database*/
        return medikamentRepository.findAll();
    }

    public List<Medikament> getAvailableMeds() {
        // return all Meds, where vorrat is not 0
        return medikamentRepository.findAvailableMeds();
    }

    public Medikament getSpecificMed(Long id) throws InstanceNotFoundException{
        Medikament med =  medikamentRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException("Medikament not found!"));
        return med;
    }

    public void addNewMed(Medikament medikament) throws InstanceAlreadyExistsException {
        Optional<Medikament> newMed = medikamentRepository.findById(medikament.getId());
        if (newMed.isPresent()) {
            throw new InstanceAlreadyExistsException("This Produkt is already in the database!");
        }
        medikamentRepository.save(medikament);
    }

    public void deleteMed(Long id) throws InstanceNotFoundException{
        boolean exists = medikamentRepository.existsById(id);
        if (!exists) {
            throw new InstanceNotFoundException("Produkt with id " + id + " does not exist in the Database!");
        }
        medikamentRepository.deleteById(id);
    }

    @Transactional //allows us to use transactions(= data exchange) (e.g. getter and setter)
    public void reduceVorratAfterOrder(Long id, int ordervolume) throws InstanceNotFoundException, LimitExceededException{
        boolean exists = medikamentRepository.existsById(id);
        if (!exists) {
            throw new InstanceNotFoundException("Produkt with id " + id + " does not exist!");
        }
        Medikament med = medikamentRepository.findById(id).orElse(null);
        int stock = med.getVorrat();
        if (ordervolume > stock || ordervolume < 0) {
            throw new LimitExceededException("Produkt with id " + id + " only has " + stock + " units in stock," +
                    "but " + ordervolume + " where ordered! Please choose a valid amount.");
        }
        med.setVorrat(stock - ordervolume);
    }

    @Transactional
    public void increaseVorrat(Long id, int extra) throws InstanceNotFoundException{
        boolean exists = medikamentRepository.existsById(id);
        if (!exists) {
            throw new InstanceNotFoundException("Produkt with id " + id + " does not exist!");
        }
        Medikament med = medikamentRepository.findById(id).orElse(null);

        med.setVorrat(med.getVorrat() + extra);
    }
}
