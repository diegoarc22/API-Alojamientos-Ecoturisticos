package com.daw.alojamientos.service;

import com.daw.alojamientos.dto.OpenMeteoRespuesta;
import com.daw.alojamientos.dto.PrevisionDiaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalApiService {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    private final RestClient restClient;
    private final String urlBase;

    public ExternalApiService(RestClient restClient, @Value("${app.openmeteo.url}") String urlBase) {
        this.restClient = restClient;
        this.urlBase = urlBase;
    }

    public List<PrevisionDiaDTO> obtenerPrevision(Double latitud, Double longitud, int dias) {
        List<PrevisionDiaDTO> prevision = new ArrayList<>();
        try {
            OpenMeteoRespuesta respuesta = restClient.get()
                    .uri(urlBase + "?latitude={lat}&longitude={lon}"
                                    + "&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max,uv_index_max"
                                    + "&forecast_days={dias}&timezone=auto",
                            latitud, longitud, dias)
                    .retrieve()
                    .body(OpenMeteoRespuesta.class);

            if (respuesta == null || respuesta.getDaily() == null) {
                return prevision;
            }

            OpenMeteoRespuesta.Daily daily = respuesta.getDaily();
            for (int i = 0; i < daily.getFechas().size(); i++) {
                prevision.add(new PrevisionDiaDTO(
                        LocalDate.parse(daily.getFechas().get(i)),
                        daily.getTemperaturaMax().get(i),
                        daily.getTemperaturaMin().get(i),
                        daily.getProbabilidadLluvia().get(i),
                        daily.getIndiceUv().get(i)
                ));
            }
            return prevision;
        } catch (Exception e) {
            // FALLBACK: sin internet, timeout o limite diario -> lista vacia, nunca un 500
            log.warn("Open-Meteo no disponible, se usa el fallback: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
