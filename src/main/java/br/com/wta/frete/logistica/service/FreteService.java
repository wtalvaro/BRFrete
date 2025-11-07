package br.com.wta.frete.logistica.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.logistica.controller.dto.FreteResponse;
import br.com.wta.frete.logistica.controller.dto.ItemFreteRequest;
import br.com.wta.frete.logistica.entity.Frete;
import br.com.wta.frete.logistica.entity.ItemFrete;
import br.com.wta.frete.logistica.entity.ModalidadeFrete;
import br.com.wta.frete.logistica.entity.OrdemServico;
import br.com.wta.frete.logistica.entity.StatusLeilao;
import br.com.wta.frete.logistica.repository.FreteRepository;
import br.com.wta.frete.logistica.repository.ItemFreteRepository;
import br.com.wta.frete.logistica.repository.ModalidadeFreteRepository;
import br.com.wta.frete.logistica.repository.StatusLeilaoRepository;
import br.com.wta.frete.logistica.service.mapper.FreteMapper;
import br.com.wta.frete.logistica.service.mapper.ItemFreteMapper;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import br.com.wta.frete.shared.service.GeoService;

/**
 * Serviço responsável por criar o objeto Frete a partir de uma Ordem de Serviço
 * (OS).
 * Orquestra o cálculo de distância, preço ANTT e inicia o ciclo de leilão.
 */
@Service
public class FreteService {

    private final FreteRepository freteRepository;
    private final FreteMapper freteMapper;
    private final ItemFreteRepository itemFreteRepository;
    private final ItemFreteMapper itemFreteMapper;
    private final GeoService geoService;
    private final AnttParametroService anttService;
    private final StatusLeilaoRepository statusLeilaoRepository;
    private final ModalidadeFreteRepository modalidadeFreteRepository;

    // Injeção de dependências via construtor
    public FreteService(
            FreteRepository freteRepository,
            FreteMapper freteMapper,
            ItemFreteRepository itemFreteRepository,
            ItemFreteMapper itemFreteMapper,
            GeoService geoService,
            AnttParametroService anttService,
            StatusLeilaoRepository statusLeilaoRepository,
            ModalidadeFreteRepository modalidadeFreteRepository) {
        this.freteRepository = freteRepository;
        this.freteMapper = freteMapper;
        this.itemFreteRepository = itemFreteRepository;
        this.itemFreteMapper = itemFreteMapper;
        this.geoService = geoService;
        this.anttService = anttService;
        this.statusLeilaoRepository = statusLeilaoRepository;
        this.modalidadeFreteRepository = modalidadeFreteRepository;
    }

    /**
     * MÉTODO CENTRAL: Inicia o ciclo de leilão/cotação criando a entidade Frete.
     * <p>
     * Responsável pela orquestração da criação e persistência do Frete e seus
     * itens,
     * delegando todos os cálculos e inferências de regras de negócio (como a
     * precificação)
     * a métodos auxiliares para aderir ao SRP.
     * </p>
     *
     * @param ordemServico           A Ordem de Serviço recém-criada.
     * @param itensFreteRequests     DTOs dos itens a serem transportados.
     * @param nomeModalidadeDesejada Nome da Modalidade de Frete a ser usada no
     *                               Frete.
     * @return FreteResponse com os dados do leilão inicial.
     * @throws ResourceNotFoundException Se o Status de Leilão ou a Modalidade de
     *                                   Frete desejada não forem encontrados.
     */
    @Transactional
    public FreteResponse criarFrete(
            OrdemServico ordemServico,
            List<ItemFreteRequest> itensFreteRequests,
            String nomeModalidadeDesejada) { // <--- NOVO PARÂMETRO: Torna a escolha da modalidade flexível

        // 1. CÁLCULO DE PARÂMETROS E INFERÊNCIA DE RECURSOS (Lógica delegada)
        FreteParametrosCalculados params = calcularParametrosIniciais(
                ordemServico,
                itensFreteRequests,
                nomeModalidadeDesejada);

        // 2. CRIAÇÃO DA ENTIDADE FRETE (Orquestração)
        Frete novoFrete = new Frete();
        novoFrete.setOrdemServico(ordemServico);

        // Aplica os parâmetros calculados/inferidos
        novoFrete.setModalidade(params.modalidade);
        novoFrete.setStatusLeilao(params.statusInicial);
        novoFrete.setDistanciaKm(params.distanciaKm);
        novoFrete.setAnttPisoMinimo(params.anttPisoMinimo);
        novoFrete.setPrecoSugerido(params.precoSugerido);
        novoFrete.setCustoBaseMercado(params.custoBaseMercado);
        novoFrete.setDataExpiracaoNegociacao(params.dataExpiracaoNegociacao);

        // ** (Os campos que antes estavam no Frete.java mas não foram mais fornecidos
        // são omitidos daqui. Ex: setTipoEmbalagem) **

        Frete freteSalvo = freteRepository.save(novoFrete);

        // 3. CRIAÇÃO E ASSOCIAÇÃO DOS ITENS DE FRETE (Lógica delegada)
        salvarItensFrete(freteSalvo, itensFreteRequests);

        return freteMapper.toResponse(freteSalvo);
    }

    /**
     * Busca um Frete pelo seu ID.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public FreteResponse buscarPorId(Long id) {
        Frete frete = freteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Frete não encontrado com ID: " + id));
        return freteMapper.toResponse(frete);
    }

    // --- MÉTODOS AUXILIARES PRIVADOS (Aumento de SRP) ---

    /**
     * Estrutura interna para encapsular todos os parâmetros calculados/inferidos.
     */
    private record FreteParametrosCalculados(
            BigDecimal pesoTotalKg,
            BigDecimal distanciaKm,
            BigDecimal anttPisoMinimo,
            BigDecimal precoSugerido,
            BigDecimal custoBaseMercado,
            LocalDateTime dataExpiracaoNegociacao,
            StatusLeilao statusInicial,
            ModalidadeFrete modalidade) {
    }

    /**
     * Responsabilidade: Realiza todos os cálculos e inferências de regras de
     * negócio
     * (peso, distância, preços, lookup de entidades mestre).
     */
    private FreteParametrosCalculados calcularParametrosIniciais(
            OrdemServico ordemServico,
            List<ItemFreteRequest> itensFreteRequests,
            String nomeModalidadeDesejada) {

        // 1. CÁLCULO DE RECURSOS E DISTÂNCIA
        BigDecimal pesoTotalKg = calcularPesoTotal(itensFreteRequests);
        BigDecimal distanciaKm = geoService.calcularDistanciaRodoviaria(
                ordemServico.getCepColeta(),
                ordemServico.getCepDestino());

        // 2. CÁLCULO DE PREÇOS BASE (LEGALIDADE)
        BigDecimal anttPisoMinimo = anttService.calcularPisoMinimo(distanciaKm, pesoTotalKg);

        // 3. OBTENÇÃO DE ENTIDADES MESTRAS (Status e Modalidade)
        StatusLeilao statusInicial = buscarStatusLeilao("AGUARDANDO_LANCES");

        // FLEXIBILIDADE: A modalidade é determinada pelo parâmetro de entrada
        ModalidadeFrete modalidade = buscarModalidadeFrete(nomeModalidadeDesejada);

        // 4. Lógica de Preço Sugerido (Mercado)
        // Exemplo: 20% acima do piso ANTT
        BigDecimal precoSugerido = anttPisoMinimo.multiply(new BigDecimal("1.20")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal custoBaseMercado = precoSugerido;

        // 5. Define a data de expiração
        LocalDateTime dataExpiracaoNegociacao = LocalDateTime.now().plusHours(48);

        return new FreteParametrosCalculados(
                pesoTotalKg,
                distanciaKm,
                anttPisoMinimo,
                precoSugerido,
                custoBaseMercado,
                dataExpiracaoNegociacao,
                statusInicial,
                modalidade);
    }

    // ... (restante dos métodos auxiliares, buscarPesoTotal, salvarItensFrete,
    // buscarStatusLeilao, buscarModalidadeFrete inalterados) ...

    private BigDecimal calcularPesoTotal(List<ItemFreteRequest> itens) {
        return itens.stream()
                .map(ItemFreteRequest::pesoEstimadoKg)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @SuppressWarnings("null")
    private void salvarItensFrete(Frete frete, List<ItemFreteRequest> itensFreteRequests) {
        // Mapeia o DTO para a Entidade, associa o Frete, e salva.
        List<ItemFrete> itens = itensFreteRequests.stream()
                .map(itemFreteMapper::toEntity)
                .peek(item -> item.setFrete(frete))
                .collect(Collectors.toList());

        itemFreteRepository.saveAll(itens);
    }

    private StatusLeilao buscarStatusLeilao(String nomeStatus) {
        return statusLeilaoRepository.findByNomeStatus(nomeStatus)
                .orElseThrow(() -> new ResourceNotFoundException("Status de Leilão não encontrado: " + nomeStatus));
    }

    private ModalidadeFrete buscarModalidadeFrete(String nomeModalidade) {
        return modalidadeFreteRepository.findByNomeModalidade(nomeModalidade)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Modalidade de Frete não encontrada: " + nomeModalidade));
    }
}