package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MedikamentService {
    /*    Responsible for business logic*/

    /*    Get Repository for data access:*/
    private final MedikamentRepository medikamentRepository;

    @Autowired //Dependency injection (makes sure the Service gets passed an instance of Repository - without that
    //annotation we would have to write this.medRepository = new medRepository, which would work as well but is unconventional
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

    public Optional<Medikament> getSpecificMed(Long id) {
        return medikamentRepository.findById(id);
    }

    public void addNewMed(Medikament medikament) {
        Optional<Medikament> newMed = medikamentRepository.findMedById(medikament.getId());
        if (newMed.isPresent()) {
            throw new IllegalStateException("This Produkt is already in the database!");
        }
        medikamentRepository.save(medikament);
    }

    public void deleteMed(Long id) {
        boolean exists = medikamentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Produkt with id " + id + " does not exist!");
        }
        medikamentRepository.deleteById(id);
    }

    @Transactional //allows us to use transactions(= data exchange) (e.g. getter and setter)
    public void reduceVorratAfterOrder(Long id, int ordervolume) {
        boolean exists = medikamentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Produkt with id " + id + " does not exist!");
        }
        Medikament med = medikamentRepository.findById(id).orElse(null);
        int stock = med.getVorrat();
        if (ordervolume > stock || ordervolume < 0) {
            throw new IllegalStateException("Produkt with id " + id + " only has " + stock + " units in stock," +
                    "but " + ordervolume + " where ordered! Please choose a valid amount.");
        }
        med.setVorrat(stock - ordervolume);
    }

    @Transactional
    public void increaseVorrat(Long id, int extra) {
        boolean exists = medikamentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Produkt with id " + id + " does not exist!");
        }
        Medikament med = medikamentRepository.findById(id).orElse(null);
        if (extra < 0) {
            throw new IllegalStateException("Illegal value: " + extra + ". Negative numbers cannot be added to the stock!");
        }
        med.setVorrat(med.getVorrat() + extra);
    }
}
