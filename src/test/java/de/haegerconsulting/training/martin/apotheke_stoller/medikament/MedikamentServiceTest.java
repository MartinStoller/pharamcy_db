package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.naming.LimitExceededException;
import java.util.Optional;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class) //generates some Code (e.g. autocloseable, after each method) needed for mocking
/*@SpringBootTest */// start springboot for the tests (important for testing the @Validation, which happens within Springboot and not within my functions
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

    @Captor
    ArgumentCaptor<Medikament> argumentCaptorMed = ArgumentCaptor.forClass(Medikament.class); //initiate AC

    @Test
    void TestValidAddNewMed() throws InstanceAlreadyExistsException {
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
        Mockito.verify(mockedMedRepository).save(argumentCaptorMed.capture()); //check if save() is called AND on which Argument
        Assertions.assertEquals(argumentCaptorMed.getValue(), med); //check if that Argument save() was called upon is the right one
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
        Assertions.assertThrows(InstanceAlreadyExistsException.class, () -> {testedMedikamentService.addNewMed(existingMed);});
    }

    @Captor
    ArgumentCaptor<Long> argumentCaptorLong; //initiate AC

    @Test
    void TestDeleteValidMed() throws InstanceNotFoundException {
        //given
        Long id = 12345678L;
        BDDMockito.given(mockedMedRepository.existsById(id)).willReturn(Boolean.TRUE);

        //when
        testedMedikamentService.deleteMed(id);

        //then
        Mockito.verify(mockedMedRepository).deleteById(argumentCaptorLong.capture());
        Assertions.assertEquals(argumentCaptorLong.getValue(), id);
    }

    @Test
    void TestDeleteInvalidMed() {
        //given
        Long id = 12345678L;
        BDDMockito.given(mockedMedRepository.existsById(id)).willReturn(Boolean.FALSE);

        //when
        Assertions.assertThrows(InstanceNotFoundException.class, () -> {testedMedikamentService.deleteMed(id);});
    }

    public static Stream<int[]> getValuesForTestValidreduceVorratAfterOrder() {
        return Stream.of(new int[]{100, 20, 80}, new int[]{50, 50, 0}, new int[]{10_000, 9_999, 1});
    }

    @ParameterizedTest
    @MethodSource("getValuesForTestValidreduceVorratAfterOrder")
    void TestValidreduceVorratAfterOrder(int[] data) throws InstanceNotFoundException, LimitExceededException {
        //given
        Long id = 12345678L;
        Medikament med = new Medikament(
                12345123L,
                "lululu",
                "lilili",
                "lalala",
                222);
        int stock = data[0];
        int ordervolume = data[1];
        int expected = data[2];
        med.setVorrat(stock);
        BDDMockito.given(mockedMedRepository.existsById(id)).willReturn(Boolean.TRUE);
        BDDMockito.given(mockedMedRepository.findById(id)).willReturn(Optional.of(med));

        //when
        testedMedikamentService.reduceVorratAfterOrder(id, ordervolume);

        //then
        Assertions.assertEquals(med.getVorrat(), expected);
    }

    @Test
    void TestInstanceNotFoundReduceVorratAfterOrder() {
        //given
        Long id = 12345678L;
        BDDMockito.given(mockedMedRepository.existsById(id)).willReturn(Boolean.FALSE);

        //then
        Assertions.assertThrows(InstanceNotFoundException.class,
                () -> {testedMedikamentService.reduceVorratAfterOrder(id, 22);});

    }

    public static Stream<int[]> getInvalidValuesForReduceVorratTest(){
        return Stream.of(new int[] {10, -1}, new int[] {10, 11}, new int[] {0, 1});
    }

    @ParameterizedTest
    @MethodSource("getInvalidValuesForReduceVorratTest")
    void TestLimitExceededExceptionReduceVorratAfterOrder(int[] data) {
        //given
        Long id = 12345678L;
        Medikament med = new Medikament(
                12345123L,
                "lululu",
                "lilili",
                "lalala",
                222);
        int stock = data[0];
        int ordervolume = data[1];
        med.setVorrat(stock);
        BDDMockito.given(mockedMedRepository.findById(id)).willReturn(Optional.of(med));
        BDDMockito.given(mockedMedRepository.existsById(id)).willReturn(Boolean.TRUE);

        //then
        Assertions.assertThrows(LimitExceededException.class,
                () -> {testedMedikamentService.reduceVorratAfterOrder(id, ordervolume);});

    }

    @Test
    void TestNotFoundExceptionInIncreaseVorrat() {
        //given
        Long id = 12345678L;
        BDDMockito.given(mockedMedRepository.existsById(id)).willReturn(Boolean.FALSE);

        Assertions.assertThrows(InstanceNotFoundException.class,
                () -> {testedMedikamentService.increaseVorrat(id, 22);});
    }

    @Test
    void TestValidIncreaseVorrat() throws InstanceNotFoundException {
        //given
        Long id = 12345678L;
        int extra = 1;
        Medikament med = new Medikament(
                12345123L,
                "lululu",
                "lilili",
                "lalala",
                222);
        BDDMockito.given(mockedMedRepository.existsById(id)).willReturn(Boolean.TRUE);
        BDDMockito.given(mockedMedRepository.findById(id)).willReturn(Optional.of(med));

        //when
        testedMedikamentService.increaseVorrat(id, extra);

        Assertions.assertEquals(med.getVorrat(), 222+extra);
    }
}