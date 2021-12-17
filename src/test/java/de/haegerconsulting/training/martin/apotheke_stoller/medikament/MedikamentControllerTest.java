package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedikamentController.class) // replaces the @SpringbootTestannotion: the full Spring application context is started but without the server. In the brackets we write where to look for the Endpoint
public class MedikamentControllerTest {
    @MockBean // allow to mock a class or an interface and to record and verify behaviors on it. @MockBean is similar to mockito's @Mock but with Spring support
    private MedikamentService mockedMedikamentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Captor
    ArgumentCaptor<Medikament> medikamentArgumentCaptor;

    @Test
    void shouldCreateMed() throws Exception {
        Medikament medikament = new Medikament(12345678L, "la", "li", "lu", 22);
        String medString = objectMapper.writeValueAsString(medikament);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/medikament")
                .contentType(MediaType.APPLICATION_JSON)
                .content(medString))
                .andExpect(handler().handlerType(MedikamentController.class)) //asserts the type of th ehandler, which processed the request
                .andExpect(handler().methodName("registerNewMed"))  //these 2 handler lines check if the controller responds correctly to the URL Request
/*            .with(csrf()))*/ // uncomment this line if we have Spring Security and CSRF enabled, which in reality we always should, to avoid HTTP 403 Forbidden response
                .andExpect(status().isOk() //this only tests if the status is 200 after the request. -> Good start but not sufficient(the method could also do nothing as long as it throws no exception or error). I also need to test if the correct function was executed (and with the right input if necessary)
                );

        Mockito.verify(mockedMedikamentService).addNewMed(medikamentArgumentCaptor.capture()); // these 2 lines assert if the controller calls the Service correctly
        Assertions.assertEquals(medikamentArgumentCaptor.getValue(), medikament);
    }


    public static Stream<String> getInvalidMeds() {
        //potentially more elegant way: use object mapper: create object and let the object mapper create the json string for you
        String idTooShort = """
                {
                    "id": 1234527,
                    "name": "lalala",
                    "wirkstoff": "lululu",
                    "hersteller": "lelele",
                    "vorrat": 22
                }""";

        String idTooLong = """
                {
                    "id": 123452789,
                    "name": "lalala",
                    "wirkstoff": "lululu",
                    "hersteller": "lelele",
                    "vorrat": 22
                }""";

        String nameIsNone = """
                {
                    "id": 12345278,
                    "name": ,
                    "wirkstoff": "lululu",
                    "hersteller": "lelele",
                    "vorrat": 22
                }""";

        String nameIsEmpty = """
                {
                    "id": 12345278,
                    "name": "",
                    "wirkstoff": "lululu",
                    "hersteller": "lelele",
                    "vorrat": 22
                }""";

        String vorratIsNegative = """
                {
                    "id": 12345278,
                    "name": "lalala",
                    "wirkstoff": "lululu",
                    "hersteller": "lelele",
                    "vorrat": -22
                }""";

        return Stream.of(idTooShort, idTooLong, nameIsNone, nameIsEmpty, vorratIsNegative);

    }

    @ParameterizedTest //tests multiple times with different parameters
    @MethodSource("getInvalidMeds")
    void testPostInvalidMeds(String med) throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/medikament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(med))
                /* .with(csrf()))*/ // uncomment this line if we have Spring Security and CSRF enabled, which in reality we always should, to avoid HTTP 403 Forbidden response
                .andExpect(status().isBadRequest()
                );

    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2})

    void testInvalidIncreaseVorrat(int extra) throws Exception{
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/medikament/increaseVorratBy/33445566").param("extra", String.valueOf(extra)))
                .andExpect(status().isBadRequest());
    }

}
