package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BestellungServiceTest {
    @Mock
    private BestellungRepository mockedBestellungRepository;
    private BestellungService testedBestellungService;

    @BeforeEach
    void setUp(){
        testedBestellungService = new BestellungService(mockedBestellungRepository); // create a new Service instance before each test
    }

    @Test
    void TestgetBestellungen() {
        //given
        testedBestellungService.getBestellungen();
        //then
        Mockito.verify(mockedBestellungRepository).findAll();
    }

    @Test
    void TestValidgetSpecificBestellung() throws InstanceNotFoundException {
        //given
        Long id =11223344L;
        Bestellung bestellung = new Bestellung(
                11223344L,
                10,
                100044,
                BestellungStatus.SENT,
                LocalDate.of(2021, Month.APRIL, 5));
        BDDMockito.given(mockedBestellungRepository.findById(id)).willReturn(Optional.of(bestellung));

        //when
        Bestellung received = testedBestellungService.getSpecificBestellung(id);

        //then
        Assertions.assertEquals(received, bestellung);
    }

    @Test
    void TestInvalidgetSpecificBestellung(){
        //given
        Long id =11223342L;
        BDDMockito.given(mockedBestellungRepository.findById(id)).willReturn(Optional.empty());

        //then
        Assertions.assertThrows(InstanceNotFoundException.class, () -> {testedBestellungService.getSpecificBestellung(id);});
    }

    @Captor
    ArgumentCaptor<Bestellung> argumentCaptorBestellung = ArgumentCaptor.forClass(Bestellung.class); //initiate AC

    @Test
    void TestValidaddNewOrder() throws InstanceAlreadyExistsException {
        //given
        Long id =11223344L;
        Bestellung bestellung = new Bestellung(
                11223344L,
                10,
                100044,
                BestellungStatus.SENT,
                LocalDate.of(2021, Month.APRIL, 5));
        BDDMockito.given(mockedBestellungRepository.findById(bestellung.getId())).willReturn(Optional.empty());

        //when:
        testedBestellungService.addNewOrder(bestellung);

        //then:
        Mockito.verify(mockedBestellungRepository).save(argumentCaptorBestellung.capture()); //check if save() is called AND on which Argument
        Assertions.assertEquals(argumentCaptorBestellung.getValue(), bestellung); //check if that Argument save() was called upon is the right one
    }

    @Test
    void TestInvalidAddNewOrder() {
        //given
        Long id = 11223344L;
        Bestellung bestellung = new Bestellung(
                11223344L,
                10,
                100044,
                BestellungStatus.SENT,
                LocalDate.of(2021, Month.APRIL, 5));
        BDDMockito.given(mockedBestellungRepository.findById(bestellung.getId())).willReturn(Optional.of(bestellung));

        //then
        Assertions.assertThrows(InstanceAlreadyExistsException.class, () -> {
            testedBestellungService.addNewOrder(bestellung);
        });
    }

    @Captor
    ArgumentCaptor<Long> argumentCaptorId; //initiate AC
    @Test
    void testValidDeleteOrder() throws InstanceNotFoundException {
        //given
        Long id = 2L;
        BDDMockito.given(mockedBestellungRepository.existsById(id)).willReturn(Boolean.TRUE);

        //when
        testedBestellungService.deleteExistingById(id);

        //then
        Mockito.verify(mockedBestellungRepository).deleteById(argumentCaptorId.capture());
        Assertions.assertEquals(argumentCaptorId.getValue(), id);

    }

    @Test
    void testInvalidDeleteOrder() {
        Long id = 2L;
        BDDMockito.given(mockedBestellungRepository.existsById(id)).willReturn(Boolean.FALSE);

        Assertions.assertThrows(InstanceNotFoundException.class, () -> {
            testedBestellungService.deleteExistingById(id);
        });
    }

    @Test
    void testValidChangeStatus() {
        Long id = 2L;
        BestellungStatus newStatus = BestellungStatus.CANCELED;
        Bestellung bestellung = new Bestellung(
                11223344L,
                10,
                100044,
                BestellungStatus.FULLY_PROCESSED,
                LocalDate.of(2021, Month.APRIL, 5));
        BDDMockito.given(mockedBestellungRepository.existsById(id)).willReturn(Boolean.TRUE);
        BDDMockito.given(mockedBestellungRepository.findById(id)).willReturn(Optional.of(bestellung));

        //when
        testedBestellungService.changeStatus(id, newStatus);

        //then
        Assertions.assertEquals(bestellung.getStatus(), newStatus);
    }
}