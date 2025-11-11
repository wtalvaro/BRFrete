// Caminho: src/main/java/br/com/wta/frete/logistica/service/FreteService.java
package br.com.wta.frete.logistica.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.logistica.controller.dto.FreteResponse;
import br.com.wta.frete.logistica.controller.dto.ItemFreteRequest;
import br.com.wta.frete.logistica.entity.Frete;
import br.com.wta.frete.logistica.entity.ItemFrete;
import br.com.wta.frete.logistica.entity.Lance;
import br.com.wta.frete.logistica.entity.ModalidadeFrete;
import br.com.wta.frete.logistica.entity.OrdemServico;
import br.com.wta.frete.logistica.entity.StatusLeilao;
import br.com.wta.frete.logistica.repository.FreteRepository;
import br.com.wta.frete.logistica.repository.ItemFreteRepository;
import br.com.wta.frete.logistica.repository.LanceRepository;
import br.com.wta.frete.logistica.repository.ModalidadeFreteRepository;
import br.com.wta.frete.logistica.repository.StatusLeilaoRepository;
import br.com.wta.frete.logistica.service.mapper.FreteMapper;
import br.com.wta.frete.logistica.service.mapper.ItemFreteMapper;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import br.com.wta.frete.shared.service.GeoService;

/**
 * Serviço responsável por criar o objeto Frete a partir de uma Ordem de Serviço
 * (OS), orquestrar os cálculos e gerenciar o ciclo de vida do leilão (incluindo
 * a finalização automática).
 */
@Service
public class FreteService {

        private static final Logger log = LoggerFactory.getLogger(FreteService.class);

        private final FreteRepository freteRepository;
        private final FreteMapper freteMapper;
        private final ItemFreteRepository itemFreteRepository;
        private final ItemFreteMapper itemFreteMapper;
        private final GeoService geoService;
        private final AnttParametroService anttService;
        private final StatusLeilaoRepository statusLeilaoRepository;
        private final ModalidadeFreteRepository modalidadeFreteRepository;
        private final LanceRepository lanceRepository;
        private final LeilaoFinalizacaoService leilaoFinalizacaoService; // NOVO: Injete o novo serviço
        private final MetricaTransportadorFreteService metricaService;

        // Injeção de dependências via construtor
        public FreteService(
                        FreteRepository freteRepository,
                        FreteMapper freteMapper,
                        ItemFreteRepository itemFreteRepository,
                        ItemFreteMapper itemFreteMapper,
                        GeoService geoService,
                        AnttParametroService anttService,
                        StatusLeilaoRepository statusLeilaoRepository,
                        ModalidadeFreteRepository modalidadeFreteRepository,
                        LanceRepository lanceRepository,
                        @Lazy LeilaoFinalizacaoService leilaoFinalizacaoService,
                        MetricaTransportadorFreteService metricaService) {
                this.freteRepository = freteRepository;
                this.freteMapper = freteMapper;
                this.itemFreteRepository = itemFreteRepository;
                this.itemFreteMapper = itemFreteMapper;
                this.geoService = geoService;
                this.anttService = anttService;
                this.statusLeilaoRepository = statusLeilaoRepository;
                this.modalidadeFreteRepository = modalidadeFreteRepository;
                this.lanceRepository = lanceRepository;
                this.leilaoFinalizacaoService = leilaoFinalizacaoService;
                this.metricaService = metricaService;
        }

        /**
         * MÉTODO CENTRAL: Inicia o ciclo de leilão/cotação criando a entidade Frete.
         */
        @Transactional
        public FreteResponse criarFrete(
                        OrdemServico ordemServico,
                        List<ItemFreteRequest> itensFreteRequests,
                        String nomeModalidadeDesejada) {

                // 1. CÁLCULO DE PARÂMETROS E INFERÊNCIA DE RECURSOS
                FreteParametrosCalculados params = calcularParametrosIniciais(
                                ordemServico,
                                itensFreteRequests,
                                nomeModalidadeDesejada);

                // 2. CRIAÇÃO DA ENTIDADE FRETE
                Frete novoFrete = new Frete();

                // [MUDANÇA APLICADA] O Frete agora tem PK própria (frete_id). A linha
                // setOrdemServicoId deve ser removida.
                // novoFrete.setOrdemServicoId(ordemServico.getId()); // REMOVIDO: PK é
                // autogerada (frete_id)
                novoFrete.setOrdemServico(ordemServico);

                // Aplica os parâmetros calculados/inferidos
                novoFrete.setModalidade(params.modalidade);
                novoFrete.setStatusLeilao(params.statusInicial);
                novoFrete.setDistanciaKm(params.distanciaKm);
                novoFrete.setAnttPisoMinimo(params.anttPisoMinimo);
                novoFrete.setPrecoSugerido(params.precoSugerido);
                novoFrete.setCustoBaseMercado(params.custoBaseMercado);
                novoFrete.setDataExpiracaoNegociacao(params.dataExpiracaoNegociacao);

                // Assumindo que o campo abaixo existe no Frete.java:
                // novoFrete.setValorInicialProposto(params.custoBaseMercado);
                // novoFrete.setTipoEmbalagem("");

                Frete freteSalvo = freteRepository.save(novoFrete);

                // 3. CRIAÇÃO E ASSOCIAÇÃO DOS ITENS DE FRETE
                salvarItensFrete(freteSalvo, itensFreteRequests);

                return freteMapper.toResponse(freteSalvo);
        }

        /**
         * Busca um Frete pelo seu ID.
         */
        @SuppressWarnings("null")
        @Transactional(readOnly = true)
        public FreteResponse buscarPorId(Long id) {
                // O método findById() usa a nova PK: freteId (Long)
                Frete frete = freteRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Frete não encontrado com ID: " + id));
                return freteMapper.toResponse(frete);
        }

        // --- MÉTODOS DE FINALIZAÇÃO DE LEILÃO (@Scheduled) ---

        /**
         * Verifica periodicamente (a cada 30 minutos) e processa todos os leilões de
         * frete que já atingiram sua data de expiração.
         */
        @Scheduled(fixedRateString = "${frete.leilao.finalizacao.intervalo:1800000}") // Exemplo: a cada 30 minutos
        @Transactional(readOnly = true)
        public void finalizarLeiloesExpirados() {
                log.info("Iniciando verificação de leilões expirados...");

                // 1. Busca no Repositório todos os Fretes expirados e não finalizados
                List<Frete> fretesExpirados = freteRepository.findByDataExpiracaoNegociacaoBeforeAndStatusLeilaoNomeStatus(
                                LocalDateTime.now(), "AGUARDANDO_LANCES"); // Assumindo o método de busca correto

                if (fretesExpirados.isEmpty()) {
                        log.info("Nenhum leilão expirado encontrado.");
                        return;
                }

                log.info("{} leilão(ões) expirado(s) encontrado(s). Processando...", fretesExpirados.size());

                // 2. Itera e chama o processador para cada um
                for (Frete frete : fretesExpirados) {
                        try {
                                // Chama o método de processamento principal (que contém a lógica de seleção de
                                // lance e transação)
                                processarLeilaoExpirado(frete);
                        } catch (Exception e) {
                                // Loga a falha e continua para o próximo Frete
                                log.error("Falha ao processar o Frete ID #{} durante a rotina agendada: {}",
                                                frete.getFreteId(), e.getMessage(), e);
                        }
                }
                log.info("Verificação de leilões expirados concluída.");
        }

        /**
         * Processa o encerramento de um Frete que atingiu sua data de expiração.
         * Seleciona o lance de menor valor (regra de leilão reverso).
         *
         * @param frete O Frete expirado.
         */
        @Transactional
        public void processarLeilaoExpirado(Frete frete) {
                Optional<Lance> optionalLanceVencedor = encontrarLanceVencedor(frete);

                if (optionalLanceVencedor.isPresent()) {
                        Lance lanceVencedor = optionalLanceVencedor.get();
                        log.info("Frete #{} expirou. Vencedor encontrado: Lance ID #{} com valor R$ {}.",
                                        frete.getFreteId(), lanceVencedor.getId(), lanceVencedor.getValorLance());

                        // 1. Atualiza o Frete (Tabela logistica.fretes)
                        // Assumimos que a tabela logistica.status_leilao contém
                        // "ENCERRADO_COM_VENCEDOR"
                        frete.setStatusLeilao(buscarStatusLeilao("ENCERRADO_COM_VENCEDOR"));
                        frete.setValorFinalAceito(lanceVencedor.getValorLance());
                        freteRepository.save(frete);

                        // 2. Delega a finalização do Lance e a confirmação da OS para o serviço
                        // dedicado.
                        // Esta chamada utiliza REQUIRES_NEW para transação separada.
                        leilaoFinalizacaoService.finalizarLeilaoEConfirmarOS(frete, lanceVencedor);

                } else {
                        // Nenhum lance foi feito.
                        log.info("Frete #{} expirou sem lances. Marcando como ENCERRADO_SEM_LANCES.", frete.getFreteId());
                        // Assumimos que a tabela logistica.status_leilao contém "ENCERRADO_SEM_LANCES"
                        frete.setStatusLeilao(buscarStatusLeilao("ENCERRADO_SEM_LANCES"));
                        freteRepository.save(frete);
                }
        }

        /**
         * Tenta encontrar o melhor lance (mais baixo) para um Frete expirado.
         *
         * @param frete O Frete expirado.
         * @return O Lance com o menor valor, se existir.
         */
        private Optional<Lance> encontrarLanceVencedor(Frete frete) {
                // Otimização: Delega a busca pelo menor lance diretamente ao banco de dados.
                return lanceRepository.findTopByFrete_OrdemServico_IdOrderByValorLanceAsc(frete.getOrdemServicoId());
        }

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
         * negócio (peso, distância, preços, lookup de entidades mestre).
         * LÓGICA ATUALIZADA: Agora considera a precificação customizada do
         * transportador
         * se ele estiver designado.
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
                // O Piso Mínimo ANTT é o teto INFERIOR (nunca se pode propor abaixo dele)
                BigDecimal anttPisoMinimo = anttService.calcularPisoMinimo(distanciaKm, pesoTotalKg);

                // 3. OBTENÇÃO DE ENTIDADES MESTRAS (Status e Modalidade)
                StatusLeilao statusInicial = buscarStatusLeilao("AGUARDANDO_LANCES");
                ModalidadeFrete modalidade = buscarModalidadeFrete(nomeModalidadeDesejada);

                // 4. Lógica de Preço Sugerido (Mercado / Customizado)
                BigDecimal custoBaseMercado;

                // ** NOVA LÓGICA DE PRECIFICAÇÃO CUSTOMIZADA **
                if (ordemServico.getTransportadorDesignado() != null) {
                        // CÁLCULO PERSONALIZADO (Proposta Fechada para um Transportador)
                        Long transportadorId = ordemServico.getTransportadorDesignado().getPessoaId();

                        // O valor base é o custo calculado pelo próprio transportador.
                        BigDecimal custoPersonalizado = metricaService.calcularCustoPersonalizado(
                                        transportadorId,
                                        nomeModalidadeDesejada,
                                        distanciaKm);

                        // Garante que o custo não seja menor que o mínimo legal.
                        custoBaseMercado = custoPersonalizado;

                        log.info("Cálculo customizado (Transportador ID: {}): R$ {}. Base de Mercado: R$ {}",
                                        transportadorId, custoPersonalizado, custoBaseMercado);

                } else {
                        // CÁLCULO PADRÃO (Leilão Aberto)
                        // O preço sugerido é o ANTT + Margem de Mercado.
                        BigDecimal margemPadrao = new BigDecimal("1.20"); // 20% acima do mínimo
                        custoBaseMercado = anttPisoMinimo.multiply(margemPadrao).setScale(2, RoundingMode.HALF_UP);
                        log.info("Cálculo padrão (Leilão Aberto). Base de Mercado: R$ {}", custoBaseMercado);
                }

                // O preço sugerido é o valor que o sistema espera que o frete seja negociado
                BigDecimal precoSugerido = custoBaseMercado;

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
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Status de Leilão não encontrado: " + nomeStatus));
        }

        private ModalidadeFrete buscarModalidadeFrete(String nomeModalidade) {
                return modalidadeFreteRepository.findByNomeModalidade(nomeModalidade)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                "Modalidade de Frete não encontrada: "
                                                                                + nomeModalidade));
        }
}