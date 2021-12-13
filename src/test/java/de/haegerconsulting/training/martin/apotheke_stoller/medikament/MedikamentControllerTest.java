package de.haegerconsulting.training.martin.apotheke_stoller.medikament;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
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
        String validObject = "{\n" +
                "    \"id\": 12345278,\n" +
                "    \"name\": \"lalala\",\n" +
                "    \"wirkstoff\": \"lululu\",\n" +
                "    \"hersteller\": \"lelele\",\n" +
                "    \"vorrat\": 22\n" +
                "}";

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/medikament")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validObject))
/*            .with(csrf()))*/ // uncomment this line if we have Spring Security and CSRF enabled, which in reality we always should, to avoid HTTP 403 Forbidden response
                .andExpect(status().isOk()
                );
    }


    public static Stream<String> getInvalidMeds() {
        String idTooShort = "{\n" +
                "    \"id\": 1234527,\n" +
                "    \"name\": \"lalala\",\n" +
                "    \"wirkstoff\": \"lululu\",\n" +
                "    \"hersteller\": \"lelele\",\n" +
                "    \"vorrat\": 22\n" +
                "}";

        String idTooLong = "{\n" +
                "    \"id\": 123452789,\n" +
                "    \"name\": \"lalala\",\n" +
                "    \"wirkstoff\": \"lululu\",\n" +
                "    \"hersteller\": \"lelele\",\n" +
                "    \"vorrat\": 22\n" +
                "}";

        String nameIsNone = "{\n" +
                "    \"id\": 12345278,\n" +
                "    \"name\": ,\n" +
                "    \"wirkstoff\": \"lululu\",\n" +
                "    \"hersteller\": \"lelele\",\n" +
                "    \"vorrat\": 22\n" +
                "}";

        String nameIsEmpty = "{\n" +
                "    \"id\": 12345278,\n" +
                "    \"name\": \"\",\n" +
                "    \"wirkstoff\": \"lululu\",\n" +
                "    \"hersteller\": \"lelele\",\n" +
                "    \"vorrat\": 22\n" +
                "}";

        String vorratIsNegative = "{\n" +
                "    \"id\": 12345278,\n" +
                "    \"name\": \"lalala\",\n" +
                "    \"wirkstoff\": \"lululu\",\n" +
                "    \"hersteller\": \"lelele\",\n" +
                "    \"vorrat\": -22\n" +
                "}";

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

}
