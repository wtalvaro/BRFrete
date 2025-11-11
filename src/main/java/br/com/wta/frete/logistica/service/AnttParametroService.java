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
 * Serviço responsável por gerenciar e calcular o Piso Mínimo de Frete (PMF) da
 * ANTT.
 * Este valor é crucial para o preço inicial legal do Leilão/Frete.
 * Utiliza parâmetros da tabela logistica.antt_parametros para garantir
 * atualização
 * dinâmica das regras regulatórias.
 */
@Service
public class AnttParametroService {

    // Constante para arredondamento (2 casas decimais, padrão financeiro)
    private static final int CASAS_DECIMAIS = 2;
    private static final RoundingMode MODO_ARREDONDAMENTO = RoundingMode.HALF_UP;

    private final AnttParametroRepository anttRepository;

    public AnttParametroService(AnttParametroRepository anttRepository) {
        this.anttRepository = anttRepository;
    }

    /**
     * Busca todos os parâmetros ANTT no banco de dados e os converte em um Map
     * de Chave -> Valor para acesso rápido no cálculo.
     * * @return Um Map contendo (String chave, BigDecimal valor) dos parâmetros.
     * 
     * @throws ResourceNotFoundException Se a tabela de parâmetros estiver vazia,
     *                                   impossibilitando qualquer cálculo.
     */
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
     * Calcula o Piso Mínimo de Frete (PMF) em Reais (BRL), seguindo a estrutura
     * básica da tabela da ANTT (custo fixo + custo variável + margem).
     * * NOTA: Esta é uma fórmula simplificada (Linear Model). A ANTT oficial é
     * complexa (tabelas por tipo de carga, eixos, etc.).
     * * @param distanciaKm Distância rodoviária em quilômetros.
     * 
     * @param pesoTotalKg Peso total da carga em quilogramas (fator de carga).
     * @return O valor do Piso Mínimo em BRL, arredondado para duas casas decimais.
     */
    public BigDecimal calcularPisoMinimo(BigDecimal distanciaKm, BigDecimal pesoTotalKg) {

        Map<String, BigDecimal> params = buscarParametrosComoMapa();

        // 1. OBTENÇÃO DOS PARÂMETROS
        // Se as chaves não existirem no DB, usamos um valor default seguro para evitar
        // falhas no cálculo (embora a busca acima já lance ResourceNotFound se o map
        // for vazio).
        // Aqui, garantimos o valor caso o DB tenha só alguns parâmetros.
        BigDecimal custoFixo = params.getOrDefault("CUSTO_FIXO_VIAGEM", new BigDecimal("100.00"));
        BigDecimal coeficientePorKm = params.getOrDefault("COEFICIENTE_POR_KM", new BigDecimal("0.50"));
        BigDecimal taxaAdministrativa = params.getOrDefault("TAXA_ADMINISTRATIVA", new BigDecimal("0.05"));

        // NOTA: Ignoramos o peso (pesoTotalKg) nesta versão simplificada do cálculo.
        // Em um cenário real, o peso influenciaria no coeficiente por quilômetro.

        // 2. CÁLCULO
        // Custo Variável (Distância * Coeficiente)
        BigDecimal custoVariavel = distanciaKm.multiply(coeficientePorKm);

        // Custo Total Básico (Fixo + Variável)
        BigDecimal custoTotalBasico = custoFixo.add(custoVariavel);

        // Aplicação de Margem Mínima Legal (Ex: 5% de margem)
        // Valor Final = Custo Total Básico * (1 + Taxa Administrativa)
        BigDecimal valorFinal = custoTotalBasico.multiply(BigDecimal.ONE.add(taxaAdministrativa));

        // 3. RETORNO (Garante 2 casas decimais e arredondamento)
        return valorFinal.setScale(CASAS_DECIMAIS, MODO_ARREDONDAMENTO);
    }
}