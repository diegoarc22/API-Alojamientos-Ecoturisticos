package com.daw.alojamientos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Alojamiento Prevision")
public class AlojamientoPrevisionDTO {
    @Schema(description = "ID único autogenerado", example = "1")
    private Long id;

    @Schema(description = "Nombre del alojamiento",example = "Cabaña Valle de Ordesa" )
    private String nombre;

    @Schema(description = "Provincia", example = "Madrid")
    private String provincia;

    @Schema(description = "Latitud", example = "42.64")
    private Double latitud;

    @Schema(description = "Longitud", example = "-0.06")
    private Double longitud;

    @Schema(description = "Actividades que ofrece el alojamiento" , example = "[\"senderismo\", \"kayak\", \"astroturismo\"]")
    private List<String> actividades;

    @Schema(description = "Prevision")
    private List<PrevisionDiaDTO> prevision;

    @Schema(description = "Actividades Recomendadas")
    private List<String> actividadesRecomendadas;

    @Schema(description = "Fuente")
    private String fuente;

    @Schema(description = "Datos en Vivo")
    private Boolean datosEnVivo;
}
