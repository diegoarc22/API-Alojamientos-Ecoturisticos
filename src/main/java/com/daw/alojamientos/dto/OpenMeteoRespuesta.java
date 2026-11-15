package com.daw.alojamientos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoRespuesta {

    private Daily daily;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Daily {

        @JsonProperty("time")
        private List<String> fechas;

        @JsonProperty("temperature_2m_max")
        private List<Double> temperaturaMax;

        @JsonProperty("temperature_2m_min")
        private List<Double> temperaturaMin;

        @JsonProperty("precipitation_probability_max")
        private List<Integer> probabilidadLluvia;

        @JsonProperty("uv_index_max")
        private List<Double> indiceUv;
    }
}
