package com.daw.alojamientos.repository;

import com.daw.alojamientos.model.Alojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AlojamientoRepository extends JpaRepository<Alojamiento,Long> {
    // Buscar alojamientos por provincias
    List<Alojamiento> findByProvinciaIgnoreCase(String provincia);
    // Buscar por precio maximo
    List<Alojamiento> findByPrecioNocheLessThanEqual(Double precioMaximo);
    // Buscar por certificadoeco en true
    List<Alojamiento> findByCertificadoEcoTrue();
}
