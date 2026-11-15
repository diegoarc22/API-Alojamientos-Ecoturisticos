package com.daw.alojamientos.controller;

import com.daw.alojamientos.dto.AlojamientoCreacionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AlojamientoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // En el perfil test no hay DevDataInitializer: la base de datos empieza vacia
    private AlojamientoCreacionDTO creacion(String codigoRegistro) {
        AlojamientoCreacionDTO dto = new AlojamientoCreacionDTO();
        dto.setCodigoRegistro(codigoRegistro);
        dto.setNombre("Casa Rural Sierra de Cazorla");
        dto.setTipo("CASA_RURAL");
        dto.setProvincia("Jaén");
        dto.setLatitud(37.91);
        dto.setLongitud(-2.93);
        dto.setPrecioNoche(95.0);
        dto.setCapacidad(8);
        dto.setEmailContacto("info@cazorla.eco");
        dto.setFechaApertura(LocalDate.of(2018, 4, 15));
        dto.setCertificadoEco(true);
        dto.setActividades(List.of("senderismo", "observacion-aves"));
        dto.setAceptaPoliticaSostenible(true);
        return dto;
    }

    @Test
    void recorridoCompleto_crearLeerDuplicarBorrar() throws Exception {
        String json = objectMapper.writeValueAsString(creacion("ECO-0900"));

        // 1. POST -> 201
        String respuesta = mockMvc.perform(post("/api/alojamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.codigoRegistro").value("ECO-0900"))
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(respuesta).get("id").asLong();

        // 2. GET de ese id -> 200
        mockMvc.perform(get("/api/alojamientos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Casa Rural Sierra de Cazorla"));

        // 3. El mismo POST otra vez -> 409 (codigoRegistro es unique en la base de datos)
        mockMvc.perform(post("/api/alojamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        // 4. DELETE -> 204
        mockMvc.perform(delete("/api/alojamientos/" + id))
                .andExpect(status().isNoContent());

        // 5. GET otra vez -> 404
        mockMvc.perform(get("/api/alojamientos/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void entorno_conPerfilTest_devuelveElNombreDelPerfil() throws Exception {
        mockMvc.perform(get("/api/alojamientos/entorno"))
                .andExpect(status().isOk())
                .andExpect(content().string("TEST - H2 en memoria aislada"));
    }
}
