package com.daw.alojamientos.controller;

import com.daw.alojamientos.dto.AlojamientoCreacionDTO;
import com.daw.alojamientos.dto.AlojamientoPatchDTO;
import com.daw.alojamientos.dto.AlojamientoPrevisionDTO;
import com.daw.alojamientos.dto.AlojamientoResumenDTO;
import com.daw.alojamientos.service.AlojamientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alojamientos")
@Tag(name = "Alojamientos", description = "CRUD y búsquedas de alojamientos ecoturísticos")
public class AlojamientoController {

    private final AlojamientoService alojamientoService;

    @Value("${app.entorno.nombre}")
    private String entorno;

    public AlojamientoController(AlojamientoService alojamientoService) {
        this.alojamientoService = alojamientoService;
    }

    @Operation(summary = "Lista todos los alojamientos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de alojamientos (puede estar vacía)")
    })
    @GetMapping
    public ResponseEntity<List<AlojamientoResumenDTO>> listar() {
        return ResponseEntity.ok(alojamientoService.listar());
    }

    @Operation(summary = "Busca un alojamiento por su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alojamiento encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún alojamiento con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlojamientoResumenDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alojamientoService.buscarPorId(id));
    }

    @Operation(summary = "Busca alojamientos por provincia (sin distinguir mayúsculas)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alojamientos de esa provincia")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<AlojamientoResumenDTO>> buscarPorProvincia(@RequestParam String provincia) {
        return ResponseEntity.ok(alojamientoService.buscarPorProvincia(provincia));
    }

    @Operation(summary = "Busca alojamientos con precio por noche menor o igual al indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alojamientos dentro del precio máximo")
    })
    @GetMapping("/precio-maximo")
    public ResponseEntity<List<AlojamientoResumenDTO>> buscarPorPrecioMaximo(@RequestParam Double max) {
        return ResponseEntity.ok(alojamientoService.buscarPorPrecioMaximo(max));
    }

    @Operation(summary = "Lista los alojamientos con certificado ecológico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alojamientos certificados")
    })
    @GetMapping("/certificados")
    public ResponseEntity<List<AlojamientoResumenDTO>> buscarConCertificado() {
        return ResponseEntity.ok(alojamientoService.buscarConCertificado());
    }

    @Operation(summary = "Registra un alojamiento nuevo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Alojamiento creado"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un alojamiento con ese código de registro"),
            @ApiResponse(responseCode = "422", description = "La capacidad supera el máximo de su tipo")
    })
    @PostMapping
    public ResponseEntity<AlojamientoResumenDTO> crear(@Valid @RequestBody AlojamientoCreacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alojamientoService.crear(dto));
    }

    @Operation(summary = "Sustituye todos los datos de un alojamiento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alojamiento actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos"),
            @ApiResponse(responseCode = "404", description = "No existe ningún alojamiento con ese id"),
            @ApiResponse(responseCode = "409", description = "Ya existe un alojamiento con ese código de registro"),
            @ApiResponse(responseCode = "422", description = "La capacidad supera el máximo de su tipo")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AlojamientoResumenDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody AlojamientoCreacionDTO dto) {
        return ResponseEntity.ok(alojamientoService.actualizar(id, dto));
    }

    @Operation(summary = "Modifica solo los campos enviados de un alojamiento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alojamiento actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos"),
            @ApiResponse(responseCode = "404", description = "No existe ningún alojamiento con ese id"),
            @ApiResponse(responseCode = "422", description = "La capacidad supera el máximo de su tipo")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AlojamientoResumenDTO> actualizarParcial(@PathVariable Long id,
                                                                   @Valid @RequestBody AlojamientoPatchDTO dto) {
        return ResponseEntity.ok(alojamientoService.actualizarParcial(id, dto));
    }

    @Operation(summary = "Elimina un alojamiento")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Alojamiento eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún alojamiento con ese id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alojamientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Datos del alojamiento + previsión de Open-Meteo y actividades recomendadas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Previsión obtenida (datosEnVivo=false si Open-Meteo no responde)"),
            @ApiResponse(responseCode = "404", description = "No existe ningún alojamiento con ese id"),
            @ApiResponse(responseCode = "422", description = "Los días tienen que estar entre 1 y 7")
    })
    @GetMapping("/{id}/prevision")
    public ResponseEntity<AlojamientoPrevisionDTO> obtenerPrevision(@PathVariable Long id,
                                                                    @RequestParam(defaultValue = "3") Integer dias) {
        return ResponseEntity.ok(alojamientoService.obtenerPrevision(id, dias));
    }

    @Operation(summary = "Devuelve el nombre del perfil (entorno) activo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre del entorno activo")
    })
    @GetMapping("/entorno")
    public ResponseEntity<String> entorno() {
        return ResponseEntity.ok(entorno);
    }
}
