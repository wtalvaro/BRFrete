package br.com.wta.frete.shared.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Serviço dedicado à comunicação com APIs de Geolocalização.
 * Retorna valores MOCK durante o desenvolvimento para simular a distância.
 */
@Service
public class GeoService {

    /**
     * Calcula a distância rodoviária entre dois CEPs (MOCK).
     * 
     * @param cepOrigem  CEP de partida.
     * @param cepDestino CEP de chegada.
     * @return A distância em quilômetros (BigDecimal).
     */
    public BigDecimal calcularDistanciaRodoviaria(String cepOrigem, String cepDestino) {

        // =========================================================================
        // 🚧 LÓGICA MOCK PARA DESENVOLVIMENTO 🚧
        // Em produção, aqui ocorreria a chamada HTTP à API externa.
        // =========================================================================

        if (cepOrigem.equals(cepDestino)) {
            return new BigDecimal("5.00");
        }

        // Simulação: Retorna uma distância aleatória (entre 100 e 500 km)
        double distancia = 100 + Math.random() * 400;

        return new BigDecimal(distancia).setScale(2, RoundingMode.HALF_UP);
    }
}