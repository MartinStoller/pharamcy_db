package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BestellungController.class)
public class BestellungControllerTest {

    @MockBean
    private BestellungService mockedBestellungService;

    @Autowired
    private WebApplicationContext wac; // no idea what this does, but needed for the mockMvc

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateMockMvc() {
        Assertions.assertNotNull(mockMvc);
    }

    @Test
    void shouldCreateBestellung() throws Exception {
        String validObject = """
                {
                    "id": 99999,
                    "product_id": 12345678,
                    "amount": 10,
                    "store_id": 12345,
                    "status": 1,
                    "date": "1995-12-12"
                }""";

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/bestellung")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validObject))
                .andExpect(status().isOk()
                );
    }

    public static Stream<String> getInvalidBest() {
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
    @MethodSource("getInvalidBest")
    void TestPostInvalidMeds(String bestellung) throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/bestellung")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bestellung))
                /* .with(csrf()))*/ // uncomment this line if we have Spring Security and CSRF enabled, which in reality we always should, to avoid HTTP 403 Forbidden response
                .andExpect(status().isBadRequest()
                );
    }
}
