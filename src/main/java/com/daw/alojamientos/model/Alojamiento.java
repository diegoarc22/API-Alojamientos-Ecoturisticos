package com.daw.alojamientos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="alojamientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Alojamiento ecoturistico registrado en la plataforma")
public class Alojamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autogenerado", example = "1")
    private Long id;

    @Column(nullable = false, unique = true,length = 8)
    @Schema(description = "Codigo de Registro", example = "ECO-0001")
    private String codigoRegistro;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre del alojamiento",example = "Cabaña Valle de Ordesa" )
    private String nombre;

    @Column(nullable = false, length = 20)
    @Schema(description = "Tipo de Alojamiento", example = "CASA_RURAL")
    private String tipo;

    @Column(nullable = false, length = 50)
    @Schema(description = "Provincia", example = "Madrid")
    private String provincia;

    @Column(nullable = false)
    @Schema(description = "Latitud", example = "42.64")
    private Double latitud;

    @Column(nullable = false)
    @Schema(description = "Longitud", example = "-0.06")
    private Double longitud;

    @Column(nullable = false)
    @Schema(description = "Precio por noche", example = "85.50")
    private Double precioNoche;

    @Column(nullable = false)
    @Schema(description = "Personas Maximas",example = "6")
    private Integer capacidad;

    @Column(length = 100, nullable = true)
    @Schema(description = "Email del alojamiento", example = "reservas@ordesa.eco")
    private String emailContacto;

    @Column(nullable = false)
    @Schema(description = "Fecha de Apertura",example = "2019-05-01")
    private LocalDate fechaApertura;

    @Column(nullable = false)
    @Schema(description = "Certificado Ecologico",example = "true")
    private Boolean certificadoEco;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "alojamiento_actividades", joinColumns = @JoinColumn(name = "alojamiento_id"))
    @Column(name = "actividad")
    @Schema(description = "Actividades que ofrece el alojamiento" , example = "[\"senderismo\", \"kayak\", \"astroturismo\"]")
    private List<String> actividades = new ArrayList<>();

}
