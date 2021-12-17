package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.haegerconsulting.training.martin.apotheke_stoller.medikament.MedikamentController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BestellungController.class)
public class BestellungControllerTest {

    @MockBean
    private BestellungService mockedBestellungService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Captor
    ArgumentCaptor<Bestellung> argumentCaptorBestellung = ArgumentCaptor.forClass(Bestellung.class);

    @Test
    void shouldCreateBestellung() throws Exception {
        Bestellung bestellung = new Bestellung(12345678L, 10, 12345, BestellungStatus.CANCELED, LocalDate.of(2021, Month.APRIL, 5));
        String bestellungString = objectMapper.writeValueAsString(bestellung);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/bestellung")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bestellungString))
                .andExpect(handler().handlerType(BestellungController.class)) //asserts the type of th ehandler, which processed the request
                .andExpect(handler().methodName("createNewOrder")) //these 2 handler lines assert if the URL request leads to the correct response in the controller
                .andExpect(status().isOk()
                );
        Mockito.verify(mockedBestellungService).addNewOrder(argumentCaptorBestellung.capture()); // these 2 lines check if the controller Response calls the correct service
        Assertions.assertEquals(argumentCaptorBestellung.getValue(), bestellung);
    }

    public static Stream<String> getInvalidBestellungen() {
        //potentially more elegant way: use object mapper: create object and let the object mapper create the json string for you
        String productIdNull = """
                {
                    "id": 99999,
                    "product_id":,
                    "amount": 10,
                    "store_id": 12345,
                    "status": 1,
                    "date": "1995-12-12"
                }""";
        String wrongDate = """
                {
                    "id": 99999,
                    "product_id": 12345678,
                    "amount": 10,
                    "store_id": 12345,
                    "status": 1,
                    "date": "30-12-1995"
                }""";
        String invalidStatus = """
                {
                    "id": 99999,
                    "product_id": 12345678,
                    "amount": 10,
                    "store_id": 12345,
                    "status": 6,
                    "date": "1995-12-12"
                }""";

        return Stream.of(productIdNull, wrongDate, invalidStatus);
    }

    @ParameterizedTest //tests multiple times with different parameters
    @MethodSource("getInvalidBestellungen")
    void testPostInvalidMeds(String bestellung) throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/bestellung")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bestellung))
                /* .with(csrf()))*/ // uncomment this line if we have Spring Security and CSRF enabled, which in reality we always should, to avoid HTTP 403 Forbidden response
                .andExpect(status().isBadRequest()
                );
    }
}
