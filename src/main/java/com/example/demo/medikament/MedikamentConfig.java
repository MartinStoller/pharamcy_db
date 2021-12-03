package com.example.demo.medikament;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration // States that this class will declear Bean methods and thus may be processed by the Spring container
public class MedikamentConfig {

    @Bean // A bean is an object that is instantiated, assembled, and otherwise managed by a Spring IoC container
    //this is probably also why the parameter "repository" is recognized even though it´s not decleared before.
    CommandLineRunner clr(MedikamentRepository repository){ // clr basically a 2nd program the fills my db with data
        return args -> {Medikament wunderpille = new Medikament(
                33445566L,
                "Wunderpille XY",
                "Zauberwirkstoff 3000",
                "Thüssen Krupp",
                20);

            Medikament lebenselixier = new Medikament(
                    31441561L,
                    "Martins Lebenselixier",
                    "Wirkstoff 7",
                    "Martin AG",
                    957);

            Medikament aspirin = new Medikament(
                    11001001L,
                    "Aspirin Complex",
                    "Zauberpulver",
                    "Hoffmann La Roche",
                    0);

            Medikament morphi = new Medikament(
                    14006277L,
                    "Morphimorph",
                    "Morphium",
                    "Bayer AG",
                    1);
            repository.saveAll(List.of(wunderpille, lebenselixier, aspirin, morphi));
        };
    }
}
