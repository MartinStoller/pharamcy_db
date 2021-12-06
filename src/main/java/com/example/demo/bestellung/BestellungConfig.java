package com.example.demo.bestellung;

import com.example.demo.bestellung.BestellungRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                    2
            );

            Bestellung order2 = new Bestellung(
                    12345678L,
                    2,
                    102030,
                    1
            );
            Bestellung order3 = new Bestellung(
                    12345678L,
                    21,
                    102232,
                    0
            );
            Bestellung order4 = new Bestellung(
                    12222678L,
                    8,
                    102232,
                    99
            );
            repository.saveAll(List.of(order1, order2, order3, order4));
        };
    }
}
