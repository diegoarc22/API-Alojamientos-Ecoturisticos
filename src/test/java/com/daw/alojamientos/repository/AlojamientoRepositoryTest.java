package com.daw.alojamientos.repository;

import com.daw.alojamientos.model.Alojamiento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AlojamientoRepositoryTest {

    @Autowired
    private AlojamientoRepository alojamientoRepository;

    private Alojamiento alojamiento(String codigoRegistro, String provincia, Double precio, Boolean certificadoEco) {
        return new Alojamiento(
                null,
                codigoRegistro,
                "Cabaña Valle de Ordesa",
                "CASA_RURAL",
                provincia,
                42.64,
                -0.06,
                precio,
                4,
                "reservas@ordesa.eco",
                LocalDate.of(2019, 5, 1),
                certificadoEco,
                List.of("senderismo", "kayak", "astroturismo")
        );
    }

    @Test
    void findByProvinciaIgnoreCase_encuentraSinImportarMayusculas() {
        alojamientoRepository.save(alojamiento("ECO-0001", "Huesca", 57.0, true));
        alojamientoRepository.save(alojamiento("ECO-0002", "Ávila", 60.0, true));

        List<Alojamiento> resultado = alojamientoRepository.findByProvinciaIgnoreCase("huesca");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProvincia()).isEqualTo("Huesca");
    }

    @Test
    void findByPrecioNocheLessThanEqual_incluyeElPrecioExacto() {
        alojamientoRepository.save(alojamiento("ECO-0001", "Huesca", 60.0, true));
        alojamientoRepository.save(alojamiento("ECO-0002", "Huesca", 80.0, true));
        alojamientoRepository.save(alojamiento("ECO-0003", "Huesca", 120.0, true));

        List<Alojamiento> resultado = alojamientoRepository.findByPrecioNocheLessThanEqual(80.0);

        assertThat(resultado)
                .extracting(Alojamiento::getPrecioNoche)
                .containsExactlyInAnyOrder(60.0, 80.0);
    }

    @Test
    void findByCertificadoEcoTrue_soloDevuelveLosCertificados() {
        alojamientoRepository.save(alojamiento("ECO-0001", "Huesca", 60.0, true));
        alojamientoRepository.save(alojamiento("ECO-0002", "Huesca", 60.0, false));

        List<Alojamiento> resultado = alojamientoRepository.findByCertificadoEcoTrue();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigoRegistro()).isEqualTo("ECO-0001");
    }

    @Test
    void elementCollection_guardaYRecuperaLasActividades() {
        Alojamiento guardado = alojamientoRepository.save(alojamiento("ECO-0001", "Huesca", 60.0, true));

        Alojamiento recuperado = alojamientoRepository.findById(guardado.getId()).orElseThrow();

        assertThat(recuperado.getActividades())
                .containsExactlyInAnyOrder("senderismo", "kayak", "astroturismo");
    }
}