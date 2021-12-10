package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MedikamentRepositoryTest {

    @Autowired
    private MedikamentRepository testedRepository;

    @AfterEach
    void tearDown(){
        //Since there is only one Test probably not important in this case(?), but it deletes the DB after each Test
        testedRepository.deleteAll();
    }

    @Test
    void findAvailableMedsTest() {

        Medikament wunderpille = new Medikament(
                33445566L,
                "Wunderpille XY",
                "Zauberwirkstoff 3000",
                "Thüssen Krupp",
                20);

        Medikament lebenselixier = new Medikament(
                31441561L,
                "Martins Lebenselixier",
                "Wirkstoff 7",
                "Martin AG",
                957);

        Medikament aspirin = new Medikament(
                11001001L,
                "Aspirin Complex",
                "Zauberpulver",
                "Hoffmann La Roche",
                0);

        Medikament morphi = new Medikament(
                14006277L,
                "Morphimorph",
                "Morphium",
                "Bayer AG",
                1);
        //given
        testedRepository.saveAll(List.of(wunderpille, lebenselixier, aspirin, morphi)); // loads these objects into the db

        //when
        List<Medikament> returnedList = testedRepository.findAvailableMeds();
        List<Medikament> expectedList = testedRepository.saveAll(List.of(wunderpille, lebenselixier, morphi));

        //then
        assertTrue(returnedList.size() == expectedList.size() && returnedList.containsAll(expectedList) &&
                expectedList.containsAll(returnedList));
    }

}