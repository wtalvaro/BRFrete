package br.com.wta.frete.logistica.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
     * MÉTODO CENTRAL: Cria o Frete, calcula os custos e inicia o leilão.
     * Chamado pelo OrdemServicoService.
     *
     * @param ordemServico       Entidade OS persistida.
     * @param itensFretpackage br.com.wta.frete.logistica.service;

import br.com.wta.frete.logistica.entity.OrdemServico;
import org.springframework.stereotype.Service;

@Service
public class FreteService {

    // Adicione aqui as dependências necessárias do Frete (Repository, Mapper, etc.)
    // public FreteService(...) { ... }

    public void criarFrete(OrdemServico ordemServico) {
        // Agora usa o método getId()
        System.out.println("LOG: FreteService acionado para Ordem de Serviço ID: " + ordemServico.getId());
        // **TODO: Implementar lógica de criação de Frete**
    }

    // Outros métodos de Frete (buscar, atualizar, etc.) serão adicionados depois.
}eRequests DTOs dos itens a serem transportados.
     * @return FreteResponse com os dados do leilão inicial.
     */
    @Transactional
    public FreteResponse criarFrete(OrdemServico ordemServico, List<ItemFreteRequest> itensFreteRequests) {

        // 1. CÁLCULO DE RECURSOS E DISTÂNCIA
        BigDecimal pesoTotalKg = calcularPesoTotal(itensFreteRequests);
        BigDecimal distanciaKm = geoService.calcularDistanciaRodoviaria(
                ordemServico.getCepColeta(),
                ordemServico.getCepDestino());

        // 2. CÁLCULO DE PREÇOS BASE (LEGALIDADE)
        // O ANTT Service está isolado e fornece o piso mínimo
        BigDecimal anttPisoMinimo = anttService.calcularPisoMinimo(distanciaKm, pesoTotalKg);

        // 3. OBTENÇÃO DE ENTIDADES MESTRAS (Iniciação do Leilão)
        StatusLeilao statusInicial = buscarStatusLeilao("AGUARDANDO_LANCES");
        // OBS: A modalidade deve ser determinada por regra de negócio (pode depender do
        // peso total/volume)
        ModalidadeFrete modalidadePadrao = buscarModalidadeFrete("FRACIONADO");

        // 4. CRIAÇÃO DA ENTIDADE FRETE
        Frete novoFrete = new Frete();
        novoFrete.setOrdemServico(ordemServico);
        novoFrete.setModalidade(modalidadePadrao);
        novoFrete.setStatusLeilao(statusInicial);
        novoFrete.setDistanciaKm(distanciaKm);
        novoFrete.setAnttPisoMinimo(anttPisoMinimo);

        // Lógica de Preço Sugerido (Mercado)
        // Exemplo: 20% acima do piso ANTT para atrair a atenção dos transportadores
        BigDecimal precoSugerido = anttPisoMinimo.multiply(new BigDecimal("1.20")).setScale(2, RoundingMode.HALF_UP);
        novoFrete.setPrecoSugerido(precoSugerido);
        novoFrete.setCustoBaseMercado(precoSugerido);

        // Define a data de expiração da negociação (Ex: 48 horas após a criação)
        novoFrete.setDataExpiracaoNegociacao(LocalDateTime.now().plusHours(48));

        Frete freteSalvo = freteRepository.save(novoFrete);

        // 5. CRIAÇÃO E ASSOCIAÇÃO DOS ITENS DE FRETE
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

    // --- MÉTODOS AUXILIARES PRIVADOS ---

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