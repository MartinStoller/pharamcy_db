package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.stream.Stream;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedikamentController.class) // replaces the @SpringbootTestannotion: the full Spring application context is started but without the server. In the brackets we write where to look for the Endpoint
public class MedikamentControllerTest {
    @MockBean // allow to mock a class or an interface and to record and verify behaviors on it. @MockBean is similar to mockito's @Mock but with Spring support
    private MedikamentService mockedMedikamentService;

    @Autowired
    private WebApplicationContext wac; // no idea what this does, but needed for the mockMvc

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateMockMvc(){
        Assertions.assertNotNull(mockMvc);
    }

    @Test
    void shouldCreateMed() throws Exception {
        String validObject = """
                {   "id": 12345278,
                    "name": "lalala",
                    "wirkstoff": "lululu",
                    "hersteller": "lelele",
                    "vorrat": 22
                }""";

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/medikament")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validObject))
/*            .with(csrf()))*/ // uncomment this line if we have Spring Security and CSRF enabled, which in reality we always should, to avoid HTTP 403 Forbidden response
                .andExpect(status().isOk()
                );
    }


    public static Stream<String> getInvalidMeds() {
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
