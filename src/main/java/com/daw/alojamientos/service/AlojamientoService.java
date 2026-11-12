package com.daw.alojamientos.service;

import com.daw.alojamientos.dto.AlojamientoCreacionDTO;
import com.daw.alojamientos.dto.AlojamientoPatchDTO;
import com.daw.alojamientos.dto.AlojamientoResumenDTO;
import com.daw.alojamientos.exception.RecursoNoEncontradoException;
import com.daw.alojamientos.exception.ReglaNegocioException;
import com.daw.alojamientos.model.Alojamiento;
import com.daw.alojamientos.repository.AlojamientoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AlojamientoService {
    private final AlojamientoRepository alojamientoRepository;
    public AlojamientoService(AlojamientoRepository alojamientoRepository) {
        this.alojamientoRepository = alojamientoRepository;
    }

    private AlojamientoResumenDTO aResumen(Alojamiento alojamiento){
        AlojamientoResumenDTO dto = new AlojamientoResumenDTO();
        dto.setId(alojamiento.getId());
        dto.setCodigoRegistro(alojamiento.getCodigoRegistro());
        dto.setNombre(alojamiento.getNombre());
        dto.setTipo(alojamiento.getTipo());
        dto.setProvincia(alojamiento.getProvincia());
        dto.setPrecioNoche(alojamiento.getPrecioNoche());
        dto.setCapacidad(alojamiento.getCapacidad());
        dto.setCertificadoEco(alojamiento.getCertificadoEco());
        dto.setActividades(alojamiento.getActividades());

        return dto;
    }

    private void aplicarDatos(AlojamientoCreacionDTO dto, Alojamiento alojamiento){
        alojamiento.setCodigoRegistro(dto.getCodigoRegistro());
        alojamiento.setNombre(dto.getNombre());
        alojamiento.setTipo(dto.getTipo());
        alojamiento.setProvincia(dto.getProvincia());
        alojamiento.setLatitud(dto.getLatitud());
        alojamiento.setLongitud(dto.getLongitud());
        alojamiento.setPrecioNoche(dto.getPrecioNoche());
        alojamiento.setCapacidad(dto.getCapacidad());
        alojamiento.setEmailContacto(dto.getEmailContacto());
        alojamiento.setFechaApertura(dto.getFechaApertura());
        alojamiento.setCertificadoEco(dto.getCertificadoEco());
        alojamiento.setActividades(dto.getActividades());
    }

    public List<AlojamientoResumenDTO> listar(){
        List<AlojamientoResumenDTO> listaDto = new ArrayList<>();
        for(Alojamiento alojamiento: alojamientoRepository.findAll()){
            listaDto.add(aResumen(alojamiento));
        }
        return listaDto;
    }

    public AlojamientoResumenDTO buscarPorId(Long id){
        Alojamiento alojamiento = obtenerEntidad(id);
        return aResumen(alojamiento);
    }

    public AlojamientoResumenDTO crear(AlojamientoCreacionDTO dto){
        comprobarCapacidad(dto.getTipo(), dto.getCapacidad());
        Alojamiento alojamiento = new Alojamiento();
        aplicarDatos(dto, alojamiento);

        return aResumen(alojamientoRepository.save(alojamiento));
    }

    public AlojamientoResumenDTO actualizar(Long id, AlojamientoCreacionDTO dto){
        Alojamiento alojamiento = obtenerEntidad(id);
        comprobarCapacidad(dto.getTipo(), dto.getCapacidad());
        aplicarDatos(dto, alojamiento);

        return aResumen(alojamientoRepository.save(alojamiento));
    }

    public void eliminar(Long id){
        Alojamiento alojamiento = obtenerEntidad(id);
        alojamientoRepository.delete(alojamiento);
    }

    public List<AlojamientoResumenDTO> buscarPorProvincia(String provincia){
        List<AlojamientoResumenDTO> listaDto = new ArrayList<>();

        for(Alojamiento alojamiento : alojamientoRepository.findByProvinciaIgnoreCase(provincia)){
            listaDto.add(aResumen(alojamiento));
        }

        return listaDto;
    }

    public List<AlojamientoResumenDTO> buscarPorPrecioMaximo(Double precio){
        List<AlojamientoResumenDTO> listaDto = new ArrayList<>();

        for(Alojamiento alojamiento : alojamientoRepository.findByPrecioNocheLessThanEqual(precio)){
            listaDto.add(aResumen(alojamiento));
        }

        return listaDto;
    }

    public List<AlojamientoResumenDTO> buscarConCertificado(){
        List<AlojamientoResumenDTO> listaDto = new ArrayList<>();

        for(Alojamiento alojamiento : alojamientoRepository.findByCertificadoEcoTrue()){
            listaDto.add(aResumen(alojamiento));
        }

        return listaDto;
    }

    private Alojamiento obtenerEntidad (long id){
        return alojamientoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe ningún alojamiento con id " + id));
    }

    private void comprobarCapacidad(String tipo, Integer capacidad){
        if(tipo == null || capacidad == null) return;

        int maximo = switch (tipo){
            case "CABANA" -> 8;
            case "GLAMPING" -> 4;
            case "CASA_RURAL" -> 20;
            default -> Integer.MAX_VALUE;
        };

        if(capacidad > maximo){
            throw new ReglaNegocioException(
                    "Un" + tipo + " admite como máximo " + maximo + " personas (has indicado " + capacidad + ")"
            );
        }
    }

    public AlojamientoResumenDTO actualizarParcial(Long id, AlojamientoPatchDTO dto) {
        Alojamiento alojamiento = obtenerEntidad(id);

        if (dto.getCodigoRegistro() != null) alojamiento.setCodigoRegistro(dto.getCodigoRegistro());
        if (dto.getNombre() != null) alojamiento.setNombre(dto.getNombre());
        if (dto.getTipo() != null) alojamiento.setTipo(dto.getTipo());
        if (dto.getProvincia() != null) alojamiento.setProvincia(dto.getProvincia());
        if (dto.getLatitud() != null) alojamiento.setLatitud(dto.getLatitud());
        if (dto.getLongitud() != null) alojamiento.setLongitud(dto.getLongitud());
        if (dto.getPrecioNoche() != null) alojamiento.setPrecioNoche(dto.getPrecioNoche());
        if (dto.getCapacidad() != null) alojamiento.setCapacidad(dto.getCapacidad());
        if (dto.getEmailContacto() != null) alojamiento.setEmailContacto(dto.getEmailContacto());
        if (dto.getFechaApertura() != null) alojamiento.setFechaApertura(dto.getFechaApertura());
        if (dto.getCertificadoEco() != null) alojamiento.setCertificadoEco(dto.getCertificadoEco());
        if (dto.getActividades() != null) alojamiento.setActividades(dto.getActividades());

        // Se comprueba con los valores finales de la entidad
        comprobarCapacidad(alojamiento.getTipo(), alojamiento.getCapacidad());

        return aResumen(alojamientoRepository.save(alojamiento));
    }
}
