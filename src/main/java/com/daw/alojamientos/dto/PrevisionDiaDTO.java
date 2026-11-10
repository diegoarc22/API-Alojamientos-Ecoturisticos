package com.daw.alojamientos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Prevision del día")
public class PrevisionDiaDTO {
    @Schema(description = "Fecha", example = "2026-11-10")
    private LocalDate fecha;

    @Schema(description = "Temperatura Máxima", example = "7.4")
    private Double temperaturaMax;

    @Schema(description = "Temperatura Mínima", example = "1.6")
    private Double temperaturaMin;

    @Schema(description = "Probabilidad de Lluvia", example = "96")
    private Integer probabilidadLluvia;

    @Schema(description = "Indice de UV", example = "0.25")
    private Double indiceUv;
}
