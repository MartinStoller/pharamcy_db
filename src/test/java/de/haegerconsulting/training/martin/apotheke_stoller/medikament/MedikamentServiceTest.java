package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class) //generates some Code (e.g. autocloseable, after each method) needed for mocking
/*@SpringBootTest */// start springboot for the tests (important for testing the @Validation, which happens with Springboot and not within my functions
class MedikamentServiceTest {
    @Mock private MedikamentRepository mockedMedRepository;// Since we already tested the Repo in the RepoTest file,
    // we mark it as a mock (-> as a dependency that does not deed be tested)

    private MedikamentService testedMedikamentService;

    @BeforeEach
    void setUp(){
        testedMedikamentService = new MedikamentService(mockedMedRepository); // create a new Service instance before each test
    }

    @Test
    void TestgetAllMedikamente() {
        //when
        testedMedikamentService.getMedikamente();
        //then
        Mockito.verify(mockedMedRepository).findAll();  // "verify that the findAll() method was invoked
        // (doesnt actually execute, which is probably the whole point about mocking)
    }

    @Test
    void TestgetAvailableMeds() {
        //when
        testedMedikamentService.getAvailableMeds();
        //then
        Mockito.verify(mockedMedRepository).findAvailableMeds();
    }

    @Test
    void TestValidGetSpecificMed() throws InstanceNotFoundException {
        // given
        Long id = 12345123L;
        Medikament med = new Medikament(
                12345123L,
                "lululu",
                "lilili",
                "lalala",
                222);
        BDDMockito.given(mockedMedRepository.findById(id)).willReturn(Optional.of(med));

        //when
        Medikament output = testedMedikamentService.getSpecificMed(id);

        //then
        Assertions.assertEquals(output, med);
    }

    @Test
    void TestInvalidGetSpecificMed() {
        // given
        Long id = 11223344L;
        BDDMockito.given(mockedMedRepository.findById(id)).willReturn(Optional.empty());

        //Get thrown Exception and assert if it is the correct one:
        Throwable receivedException = Assertions.assertThrows(InstanceNotFoundException.class,
                ()-> testedMedikamentService.getSpecificMed(id));
        Assertions.assertEquals("Medikament not found!", receivedException.getMessage());
    }

    @Test
    void TestValidAddNewMed() throws InstanceAlreadyExistsException, InvalidAttributeValueException {
        //given
        Medikament med = new Medikament(
                12345123L,
                "lululu",
                "lilili",
                "lalala",
                222);
        BDDMockito.given(mockedMedRepository.findById(med.getId())).willReturn(Optional.empty());

        //when
        testedMedikamentService.addNewMed(med);

        //then
        ArgumentCaptor<Medikament> argumentCaptor = ArgumentCaptor.forClass(Medikament.class); //initiate AC
        Mockito.verify(mockedMedRepository).save(argumentCaptor.capture()); //check if save() is called AND on which Argument
        Assertions.assertEquals(argumentCaptor.getValue(), med); //check if that Argument save() was called upon is the right one
    }

    @Test
    void TestInvalidAddNewMed(){
        //Tests AlreadyExistsException. The fact that AddNewMed() was invoked means that a valid Medikament object does exist
        // because it takes one as argument. -> If I want to test for empty strings etc, I need to move up to the Repository
        //given
        Medikament existingMed = new Medikament(
                12345123L,
                "lululu",
                "lilili",
                "lalala",
                222);
        BDDMockito.given(mockedMedRepository.findById(existingMed.getId())).willReturn(Optional.of(existingMed));

        //then
        Assertions.assertThrows(Exception.class, () -> {testedMedikamentService.addNewMed(existingMed);});
    }


    @Disabled
    @Test
    void deleteMed() {
    }

    @Disabled
    @Test
    void reduceVorratAfterOrder() {
    }

    @Disabled
    @Test
    void increaseVorrat() {
    }
}