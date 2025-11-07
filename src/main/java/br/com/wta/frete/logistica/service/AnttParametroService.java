package br.com.wta.frete.logistica.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.wta.frete.logistica.entity.AnttParametro;
import br.com.wta.frete.logistica.repository.AnttParametroRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

/**
 * Serviço responsável por gerenciar e calcular o Piso Mínimo de Frete da ANTT.
 * Utiliza parâmetros da tabela logistica.antt_parametros.
 */
@Service
public class AnttParametroService {

    private final AnttParametroRepository anttRepository;

    public AnttParametroService(AnttParametroRepository anttRepository) {
        this.anttRepository = anttRepository;
    }

    private Map<String, BigDecimal> buscarParametrosComoMapa() {
        List<AnttParametro> parametros = anttRepository.findAll();

        if (parametros.isEmpty()) {
            // Lançamos uma exceção para evitar que um cálculo incompleto vá para produção.
            throw new ResourceNotFoundException(
                    "Nenhum parâmetro ANTT (piso mínimo legal) encontrado. O cálculo é impossível.");
        }

        return parametros.stream()
                .collect(Collectors.toMap(
                        AnttParametro::getChave,
                        AnttParametro::getValor));
    }

    /**
     * Calcula o Piso Mínimo Legal de Frete (ANTT) baseado na distância e nos
     * pesos/volumes.
     * 
     * @param distanciaKm Distância em quilômetros.
     * @param pesoTotalKg Peso total da carga em quilogramas (usado como fator de
     *                    carga).
     * @return O valor do Piso Mínimo em BRL.
     */
    public BigDecimal calcularPisoMinimo(BigDecimal distanciaKm, BigDecimal pesoTotalKg) {

        Map<String, BigDecimal> params = buscarParametrosComoMapa();

        // --- Exemplo de Parâmetros (Adaptado para o seu modelo Chave/Valor) ---
        // Se as chaves não existirem, usamos um valor default seguro.
        BigDecimal custoFixo = params.getOrDefault("CUSTO_FIXO_VIAGEM", new BigDecimal("100.00"));
        BigDecimal coeficientePorKm = params.getOrDefault("COEFICIENTE_POR_KM", new BigDecimal("0.50"));
        BigDecimal taxaAdministrativa = params.getOrDefault("TAXA_ADMINISTRATIVA", new BigDecimal("0.05"));

        // 1. Cálculo do Custo Variável (Distância * Coeficiente)
        BigDecimal custoVariavel = distanciaKm.multiply(coeficientePorKm);

        // 2. Custo Total Básico (Fixo + Variável)
        BigDecimal custoTotalBasico = custoFixo.add(custoVariavel);

        // 3. Aplicação de Margem Mínima Legal
        // Valor Final = Custo Total Básico * (1 + Taxa Administrativa)
        BigDecimal valorFinal = custoTotalBasico.multiply(BigDecimal.ONE.add(taxaAdministrativa));

        // NOTA: Ignoramos o peso na fórmula simplificada, mas ele seria um fator
        // multiplicador aqui.

        return valorFinal.setScale(2, RoundingMode.HALF_UP);
    }
}