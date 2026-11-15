package com.daw.alojamientos.controller;

import com.daw.alojamientos.dto.AlojamientoCreacionDTO;
import com.daw.alojamientos.dto.AlojamientoResumenDTO;
import com.daw.alojamientos.exception.RecursoNoEncontradoException;
import com.daw.alojamientos.service.AlojamientoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlojamientoController.class)
class AlojamientoControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AlojamientoService alojamientoService;

    private AlojamientoResumenDTO resumen() {
        return new AlojamientoResumenDTO(
                1L, "ECO-0001", "Cabaña Valle de Ordesa", "CABANA", "Huesca",
                85.50, 6, true, List.of("senderismo", "kayak", "astroturismo"));
    }

    private AlojamientoCreacionDTO creacionValida() {
        AlojamientoCreacionDTO dto = new AlojamientoCreacionDTO();
        dto.setCodigoRegistro("ECO-0001");
        dto.setNombre("Cabaña Valle de Ordesa");
        dto.setTipo("CABANA");
        dto.setProvincia("Huesca");
        dto.setLatitud(42.64);
        dto.setLongitud(-0.06);
        dto.setPrecioNoche(85.50);
        dto.setCapacidad(6);
        dto.setEmailContacto("reservas@ordesa.eco");
        dto.setFechaApertura(LocalDate.of(2019, 5, 1));
        dto.setCertificadoEco(true);
        dto.setActividades(List.of("senderismo", "kayak", "astroturismo"));
        dto.setAceptaPoliticaSostenible(true);
        return dto;
    }

    @Test
    void buscarPorId_existe_devuelve200ConElAlojamiento() throws Exception {
        when(alojamientoService.buscarPorId(1L)).thenReturn(resumen());

        mockMvc.perform(get("/api/alojamientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigoRegistro").value("ECO-0001"))
                .andExpect(jsonPath("$.nombre").value("Cabaña Valle de Ordesa"));
    }

    @Test
    void buscarPorId_noExiste_devuelve404() throws Exception {
        when(alojamientoService.buscarPorId(99L))
                .thenThrow(new RecursoNoEncontradoException("No existe ningún alojamiento con id 99"));

        mockMvc.perform(get("/api/alojamientos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensaje").value("No existe ningún alojamiento con id 99"));
    }

    @Test
    void crear_conDatosValidos_devuelve201() throws Exception {
        when(alojamientoService.crear(any(AlojamientoCreacionDTO.class))).thenReturn(resumen());

        mockMvc.perform(post("/api/alojamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creacionValida())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("CABANA"));
    }

    @Test
    void crear_conDatosNoValidos_devuelve400YNoLlamaAlServicio() throws Exception {
        AlojamientoCreacionDTO dto = creacionValida();
        dto.setCodigoRegistro("ECO-1");   // no cumple el patron ECO-0000
        dto.setNombre("X");               // menos de 3 caracteres

        mockMvc.perform(post("/api/alojamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erroresCampo.codigoRegistro").exists())
                .andExpect(jsonPath("$.erroresCampo.nombre").exists());

        verify(alojamientoService, never()).crear(any());
    }

    @Test
    void eliminar_existe_devuelve204() throws Exception {
        mockMvc.perform(delete("/api/alojamientos/1"))
                .andExpect(status().isNoContent());

        verify(alojamientoService).eliminar(1L);
    }
}
