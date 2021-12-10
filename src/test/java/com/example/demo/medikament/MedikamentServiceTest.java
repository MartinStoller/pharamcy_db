package com.example.demo.medikament;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) //generates some Code (e.g. autocloseable, after each method) needed for mocking
class MedikamentServiceTest {
    @Mock private MedikamentRepository mockedMedRepository;// Since we already tested the Reop in the RepoTest file,
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
                ()->{testedMedikamentService.getSpecificMed(id); });
        Assertions.assertEquals("Medikament not found!", receivedException.getMessage());
    }

    @Disabled
    @Test
    void addNewMed() {
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