package com.daw.alojamientos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta de la aplicacion de los datos pedidos por el cliente")
public class AlojamientoResumenDTO {

    @Schema(description = "ID único autogenerado", example = "1")
    private Long id;

    @Schema(description = "Codigo de Registro", example = "ECO-0001")
    private String codigoRegistro;

    @Schema(description = "Nombre del alojamiento",example = "Cabaña Valle de Ordesa" )
    private String nombre;

    @Schema(description = "Tipo de Alojamiento", example = "CASA_RURAL")
    private String tipo;

    @Schema(description = "Provincia", example = "Madrid")
    private String provincia;

    @Schema(description = "Precio por noche", example = "85.50")
    private Double precioNoche;

    @Schema(description = "Personas Maximas",example = "6")
    private Integer capacidad;

    @Schema(description = "Certificado Ecologico",example = "true")
    private Boolean certificadoEco;

    @Schema(description = "Actividades que ofrece el alojamiento" , example = "[\"senderismo\', \"kayak\", \"astroturismo\"]")
    private List<String> actividades = new ArrayList<>();

}
