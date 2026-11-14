package com.daw.alojamientos.config;


import com.daw.alojamientos.model.Alojamiento;
import com.daw.alojamientos.repository.AlojamientoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
public class DevDataInitializer implements CommandLineRunner {

    private final AlojamientoRepository repository;

    public DevDataInitializer(AlojamientoRepository repository){this.repository = repository;}

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.save(new Alojamiento(null, "ECO-0001", "Cabaña Valle de Ordesa", "CABANA", "Huesca", 42.64, -0.06, 85.50, 6, "roberto2026@daw.es", LocalDate.of(2019, 5, 1), true, List.of("senderismo","kayak","astroturismo")));
        repository.save(new Alojamiento(null, "ECO-0002", "Glamping Sierra de Gredos", "GLAMPING", "Ávila", 40.26, -5.11, 110.00, 2, "claudio2026@daw.es", LocalDate.of(2023, 11, 17), true, List.of("senderismo","astroturismo")));
        repository.save(new Alojamiento(null,"ECO-0003","Casa Rural Picos de Europa","CASA_RURAL","Asturias",43.35 ,-5.13  ,140.00,12, "fernando2026@daw.es", LocalDate.of(2021,5,1),false, List.of("senderismo","ciclismo","taller-huerto")));
        repository.save(new Alojamiento(null,"ECO-0004","Ecolodge Doñana","GLAMPING","Huelva",37.13 ,-6.48  ,95.00,4, "lorenzo2026@daw.es", LocalDate.of(2024,7,28),true, List.of("observacion_aves","kayak")));
        repository.save(new Alojamiento(null, "ECO-0005", "Cabaña Bosque de Garajonay", "CABANA", "Santa Cruz de Tenerife", 28.13, -17.25, 70.00, 4, "wilson2026@daw.es", LocalDate.of(2020, 12, 13), false, List.of("senderismo","observacion-aves")));
    }
}
