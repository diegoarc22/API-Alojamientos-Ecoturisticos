package com.daw.alojamientos.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta que devuelve la API cuando ocurre un error")
public class ErrorResponse {
    @Schema(example = "404")
    private int status;

    @Schema(example = "No existe el alojamiento con id 99")
    private String mensaje;

    @Schema(example = "2026-11-11T20:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Solo en errores 400: campo y su error")
    private Map<String, String> erroresCampo;
}
