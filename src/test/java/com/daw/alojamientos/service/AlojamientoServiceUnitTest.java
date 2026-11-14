package com.daw.alojamientos.service;
import com.daw.alojamientos.dto.AlojamientoCreacionDTO;
import com.daw.alojamientos.dto.AlojamientoPatchDTO;
import com.daw.alojamientos.dto.AlojamientoResumenDTO;
import com.daw.alojamientos.exception.RecursoNoEncontradoException;
import com.daw.alojamientos.exception.ReglaNegocioException;
import com.daw.alojamientos.model.Alojamiento;
import com.daw.alojamientos.repository.AlojamientoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class AlojamientoServiceUnitTest {
    @Mock
    private AlojamientoRepository alojamientoRepository;
    @InjectMocks
    private AlojamientoService alojamientoService;
    private Alojamiento glamping() {
        return new Alojamiento(
                1L,
                "ECO-0002",
                "Glamping Sierra de Gredos",
                "GLAMPING",
                "Ávila",
                40.26,
                -5.11,
                110.0,
                2,
                "hola@gredos.eco",
                LocalDate.of(2021, 6, 1),
                true,
                List.of("astroturismo", "senderismo")
        );
    }
    private AlojamientoCreacionDTO creacion(String tipo, Integer capacidad) {
        return new AlojamientoCreacionDTO(
                "ECO-0100",
                "Casa Rural Sierra de Cazorla",
                tipo,
                "Jaén",
                37.91,
                -2.93,
                95.0,
                capacidad,
                "info@cazorla.eco",
                LocalDate.of(2018, 4, 15),
                true,
                List.of("senderismo"),
                true
        );
    }
    @Test
    void buscarPorId_existente_devuelveElResumen() {
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(glamping()));
        AlojamientoResumenDTO resultado = alojamientoService.buscarPorId(1L);
        assertThat(resultado.getCodigoRegistro()).isEqualTo("ECO-0002");
        assertThat(resultado.getActividades()).containsExactly("astroturismo",
                "senderismo");
    }
    @Test
    void buscarPorId_inexistente_lanzaRecursoNoEncontrado() {
        when(alojamientoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> alojamientoService.buscarPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("99");
    }
    @Test
    void crear_valido_guardaYDevuelveElResumen() {
        when(alojamientoRepository.save(any(Alojamiento.class))).thenAnswer(inv ->
                inv.getArgument(0));
        AlojamientoResumenDTO resultado = alojamientoService.crear(creacion("CASA_RURAL",
                8));
        assertThat(resultado.getNombre()).isEqualTo("Casa Rural Sierra de Cazorla");
        verify(alojamientoRepository).save(any(Alojamiento.class));
    }
    @Test
    void crear_glampingDemasiadoGrande_lanzaReglaNegocioYNoGuarda() {
        assertThatThrownBy(() -> alojamientoService.crear(creacion("GLAMPING", 10)))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("4");
        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }
    @Test
    void actualizarParcial_soloCambiaLosCamposEnviados() {
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(glamping()));


        when(alojamientoRepository.save(any(Alojamiento.class))).thenAnswer(inv ->
                inv.getArgument(0));
        AlojamientoPatchDTO patch = new AlojamientoPatchDTO();
        patch.setPrecioNoche(90.0);
        AlojamientoResumenDTO resultado = alojamientoService.actualizarParcial(1L, patch);
        assertThat(resultado.getPrecioNoche()).isEqualTo(90.0);
        assertThat(resultado.getNombre()).isEqualTo("Glamping Sierra de Gredos");
        assertThat(resultado.getCapacidad()).isEqualTo(2);
    }
    @Test
    void actualizarParcial_capacidadQueRompeLaRegla_lanzaReglaNegocio() {
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(glamping()));
        AlojamientoPatchDTO patch = new AlojamientoPatchDTO();
        patch.setCapacidad(10);
        assertThatThrownBy(() -> alojamientoService.actualizarParcial(1L, patch))
                .isInstanceOf(ReglaNegocioException.class);
        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }
    @Test
    void eliminar_inexistente_lanzaRecursoNoEncontradoYNoBorra() {
        when(alojamientoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> alojamientoService.eliminar(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
        verify(alojamientoRepository, never()).delete(any(Alojamiento.class));
    }
}