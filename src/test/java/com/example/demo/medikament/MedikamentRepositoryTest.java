package com.example.demo.medikament;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MedikamentRepositoryTest {

    @Autowired
    private MedikamentRepository testedRepository;

    @AfterEach
    void tearDown(){
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


    @Test
    void findExistingMedByIdTest() {
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
        testedRepository.saveAll(List.of(wunderpille, lebenselixier, aspirin, morphi)); // loads these objects into the db
        //given
        Long id = 14006277L;
        //when
        Optional<Medikament> returned_med = testedRepository.findById(14006277L);
        //then
        Optional<Medikament> expected_med = Optional.of(morphi);

        assertEquals(returned_med, expected_med);
    }

}