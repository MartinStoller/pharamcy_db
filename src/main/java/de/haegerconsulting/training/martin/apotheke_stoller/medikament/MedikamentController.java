package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.naming.LimitExceededException;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.List;

@RestController
@RequestMapping(path = "/medikament")
@Validated
public class MedikamentController {
/*    Class, which contains all the resources for our API -> functions the user can call*/
    private final MedikamentService medikamentService;
    @Autowired //Dependency injection (makes sure a Service instance gets passed to Controller - without that
    //annotation we would have to write this.medService = new medService, which would work as well but is unconventional
    //In other words: a Controller-Constructor is autowired when creating the Bean.
    public MedikamentController(MedikamentService medikamentService) {
        this.medikamentService = medikamentService;
    }

    @GetMapping(path="/all")
    public List<Medikament> getMedikamente() {
        return medikamentService.getMedikamente();
    }

    @GetMapping(path="/{id}")
    public Medikament getSpecificMed(@PathVariable("id") Long id) throws InstanceNotFoundException{
        return medikamentService.getSpecificMed(id);
    }

    @PostMapping
    public ResponseEntity<String> registerNewMed(@Valid @RequestBody Medikament medikament) throws InstanceAlreadyExistsException {
        //@Valid validates the requestbody based on the constraints, which we have set. If the validation fails,
        // it will trigger a MethodArgumentNotValidException. By default, Spring will translate this exception to a HTTP status 400 (Bad Request).
        // We take the Response Body as input(Annotation) and map it into a Med (Datatype Medikament), which
        // then can get added to the db by the method
        medikamentService.addNewMed(medikament);
        return ResponseEntity.ok("valid");
    }

    @GetMapping(path="/allAvailable")
    public List<Medikament> getAvailableMeds() {
    return medikamentService.getAvailableMeds();
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT) // replaces the returnResponse Entity by returning a Responsestatus
    @DeleteMapping(path="/{id}")
    public void deleteMed(@PathVariable("id") Long id) throws InstanceNotFoundException {
        medikamentService.deleteMed(id);
/*        return ResponseEntity.ok("valid");*/
    }

    @PutMapping(path="/reduceStockAfterOrder/{id}/{ordervolume}") // I could also do this with a RequestParam such as below
    public ResponseEntity<String> reduceVorratAfterOrder(@PathVariable("id") Long id,
                                                         @PathVariable("ordervolume") int ordervolume)
            throws InstanceNotFoundException, LimitExceededException {
        medikamentService.reduceVorratAfterOrder(id, ordervolume);
        return ResponseEntity.ok("valid");
    }

/*    @ControllerAdvice
    public class ExceptionhandlerForConstraintViolationException{
        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(ConstraintViolationException.class)
        ApiError cve(ConstraintViolationException)(ConstraintViolationException e) {
            return BAD_REQUEST.apply
        }
    }*/

    @PutMapping(path="/increaseVorratBy/{id}") // RequestParam is the alternative to multiple PathVariables
    public ResponseEntity<String> increaseVorrat(@PathVariable("id") Long id,
                               @RequestParam @Min(value = 1, message = "Ordervolume must be at least 1!") int extra)
            throws InstanceNotFoundException{
        medikamentService.increaseVorrat(id, extra);
        return ResponseEntity.ok("valid");
    }

}
