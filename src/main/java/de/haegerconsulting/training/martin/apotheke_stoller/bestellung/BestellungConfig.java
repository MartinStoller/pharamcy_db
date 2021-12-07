package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class BestellungConfig {
    @Bean
    CommandLineRunner clr_orders(BestellungRepository repository) {
        return args -> {
            Bestellung order1 = new Bestellung(
                    11223344L,
                    10,
                    100044,
                    2,
                    LocalDate.of(2021, Month.APRIL, 5)
            );

            Bestellung order2 = new Bestellung(
                    12345678L,
                    2,
                    102030,
                    1,
                    LocalDate.of(2020, Month.AUGUST, 30)
            );
            Bestellung order3 = new Bestellung(
                    12345678L,
                    21,
                    102232,
                    0,
                    LocalDate.of(2021, Month.AUGUST, 21)
            );
            Bestellung order4 = new Bestellung(
                    12222678L,
                    8,
                    102232,
                    99,
                    LocalDate.of(2019, Month.JULY, 1)
            );
            repository.saveAll(List.of(order1, order2, order3, order4));
        };
    }
}
