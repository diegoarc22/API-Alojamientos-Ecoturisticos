package com.daw.alojamientos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar un alojamiento")
public class AlojamientoCreacionDTO {
    @Schema(description = "Codigo de Registro", example = "ECO-0001")
    @NotBlank(message = "El código de registro es obligatorio")
    @Pattern(regexp = "^ECO-\\d{4}$", message = "El código de registro debe tener el formato 'ECO-0000'")
    private String codigoRegistro;

    @Schema(description = "Nombre del alojamiento",example = "Cabaña Valle de Ordesa" )
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre tiene que tener entre 3 y 100 caracteres")
    private String nombre;

    @Schema(description = "Tipo de Alojamiento", example = "CASA_RURAL")
    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "^(CABANA|GLAMPING|CASA_RURAL)$", message = "El tipo debe ser CABANA, GLAMPING o CASA_RURAL")
    private String tipo;

    @Schema(description = "Provincia", example = "Madrid")
    @NotBlank(message = "La provincia es obligatoria")
    private String provincia;

    @Schema(description = "Latitud", example = "42.64")
    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90", message = "La latitud tiene que ser mayor o igual que -90")
    @DecimalMax(value = "90", message = "La latitud tiene que ser menor o igual que 90")
    private Double latitud;

    @Schema(description = "Longitud", example = "-0.06")
    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180", message = "La longitud tiene que ser mayor o igual que -180")
    @DecimalMax(value = "180", message = "La longitud tiene que ser menor o igual que 180")
    private Double longitud;

    @Schema(description = "Precio por noche", example = "85.50")
    @NotNull(message = "El precio de la noche es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    @Digits(integer = 4, fraction = 2, message = "El precio debe tener como máximo 4 cifras enteras y 2 decimales")
    private Double precioNoche;

    @Schema(description = "Personas Maximas", example = "6")
    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad mínima es de 1 persona")
    @Max(value = 20, message = "La capacidad máxima es de 20 personas")
    private Integer capacidad;

    @Schema(description = "Email del alojamiento", example = "reservas@ordesa.eco")
    @Email(message = "Debe tener formato de email.")
    private String emailContacto;

    @Schema(description = "Fecha de Apertura", example = "2019-05-01")
    @NotNull(message = "La fecha de apertura es obligatoria")
    @PastOrPresent(message = "La fecha de apertura tiene que ser pasada o presente")
    private LocalDate fechaApertura;

    @Schema(description = "Certificado Ecológico", example = "true")
    @NotNull(message = "El campo certificado eco es obligatorio")
    private Boolean certificadoEco;

    @Schema(description = "Actividades que ofrece el alojamiento", example = "[[\"senderismo\", \"kayak\", \"astroturismo\"]]")
    @NotEmpty(message = "Debe tener al menos una actividad")
    private List<String> actividades;

    @Schema(description = "Política Sostenible", example = "true")
    @NotNull(message = "El campo Política sostenible es obligatorio")
    @AssertTrue(message = "Debes aceptar la política de sostenibilidad")
    private Boolean aceptaPoliticaSostenible;
}
