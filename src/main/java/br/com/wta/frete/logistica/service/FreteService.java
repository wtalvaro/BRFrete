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
                        LanceRepository lanceRepository) {
                this.freteRepository = freteRepository;
                this.freteMapper = freteMapper;
                this.itemFreteRepository = itemFreteRepository;
                this.itemFreteMapper = itemFreteMapper;
                this.geoService = geoService;
                this.anttService = anttService;
                this.statusLeilaoRepository = statusLeilaoRepository;
                this.modalidadeFreteRepository = modalidadeFreteRepository;
                this.lanceRepository = lanceRepository;
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
         * TAREFA AGENDADA: Verifica e finaliza leilões de frete que expiraram.
         * Rodará a cada 60 segundos.
         * Deve ter a anotação @EnableScheduling na classe principal do projeto.
         */
        @Scheduled(fixedRate = 60000)
        @Transactional
        public void finalizarLeiloesExpirados() {
                log.info("Iniciando verificação de leilões expirados...");

                final String STATUS_ABERTO = "AGUARDANDO_LANCES"; // Status que indica que o leilão está ativo

                // Busca fretes expirados que ainda estão no status de negociação
                List<Frete> fretesExpirados = freteRepository
                                .findByDataExpiracaoNegociacaoBeforeAndStatusLeilaoNomeStatus(
                                                LocalDateTime.now(), STATUS_ABERTO);

                if (fretesExpirados.isEmpty()) {
                        log.info("Nenhum leilão expirado encontrado.");
                        return;
                }

                log.info("Processando {} leilões expirados...", fretesExpirados.size());
                fretesExpirados.forEach(this::processarFinalizacao);
        }

        /**
         * Lógica principal para finalizar um frete, selecionando o melhor lance.
         * NOTA: Este método usa frete.getOrdemServicoId(). Assumimos que a entidade
         * Frete.java agora possui um getter auxiliar que retorna
         * frete.getOrdemServico().getId().
         */
        private void processarFinalizacao(Frete frete) {
                // 1. Busca o melhor lance (o menor valor) para o leilão reverso
                Optional<Lance> optionalLanceVencedor = lanceRepository
                                .findTopByFreteOrdemServico_IdOrderByValorPropostoAsc(frete.getOrdemServicoId());

                if (optionalLanceVencedor.isPresent()) {
                        Lance lanceVencedor = optionalLanceVencedor.get();
                        finalizarComVencedor(frete, lanceVencedor);
                } else {
                        finalizarSemLances(frete);
                }
        }

        /**
         * Atualiza o Frete e o Lance quando há um vencedor.
         */
        private void finalizarComVencedor(Frete frete, Lance lanceVencedor) {
                try {
                        // 1. Atualiza o Lance como vencedor
                        lanceVencedor.setVencedor(true);
                        lanceRepository.save(lanceVencedor);

                        // 2. Atualiza o Frete
                        StatusLeilao statusVencedor = buscarStatusLeilao("ENCERRADO_COM_VENCEDOR");

                        frete.setStatusLeilao(statusVencedor);
                        frete.setValorFinalAceito(lanceVencedor.getValorLance());
                        freteRepository.save(frete);

                        log.info("Leilão {} finalizado com sucesso. Vencedor: Transportador ID {} com lance R$ {}.",
                                        frete.getOrdemServicoId(), lanceVencedor.getTransportador().getPessoaId(),
                                        lanceVencedor.getValorLance());

                } catch (ResourceNotFoundException e) {
                        log.error("Erro fatal ao finalizar leilão {}: Status 'ENCERRADO_COM_VENCEDOR' não encontrado no DB. O frete permanece no status AGUARDANDO_LANCES.",
                                        frete.getOrdemServicoId(), e);
                }
        }

        /**
         * Atualiza o Frete quando não há lances válidos.
         */
        private void finalizarSemLances(Frete frete) {
                try {
                        StatusLeilao statusSemLances = buscarStatusLeilao("ENCERRADO_SEM_LANCES");

                        frete.setStatusLeilao(statusSemLances);
                        frete.setValorFinalAceito(null);
                        freteRepository.save(frete);

                        log.warn("Leilão {} finalizado sem lances. Status atualizado para: ENCERRADO_SEM_LANCES.",
                                        frete.getOrdemServicoId());

                } catch (ResourceNotFoundException e) {
                        log.error("Erro fatal ao finalizar leilão {}: Status 'ENCERRADO_SEM_LANCES' não encontrado no DB. O frete permanece no status AGUARDANDO_LANCES.",
                                        frete.getOrdemServicoId(), e);
                }
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
         * negócio (peso, distância, preços, lookup de entidades mestre).
         */
        private FreteParametrosCalculados calcularParametrosIniciais(
                        OrdemServico ordemServico,
                        List<ItemFreteRequest> itensFreteRequests,
                        String nomeModalidadeDesejada) {

                // 1. CÁLCULO DE RECURSOS E DISTÂNCIA
                BigDecimal pesoTotalKg = calcularPesoTotal(itensFreteRequests);
                // Assumindo que o GeoService calcula a distância com base nos CEPs da OS
                BigDecimal distanciaKm = geoService.calcularDistanciaRodoviaria(
                                ordemServico.getCepColeta(),
                                ordemServico.getCepDestino());

                // 2. CÁLCULO DE PREÇOS BASE (LEGALIDADE)
                // Assume que o AnttParametroService está injetado
                BigDecimal anttPisoMinimo = anttService.calcularPisoMinimo(distanciaKm, pesoTotalKg);

                // 3. OBTENÇÃO DE ENTIDADES MESTRAS (Status e Modalidade)
                StatusLeilao statusInicial = buscarStatusLeilao("AGUARDANDO_LANCES");
                ModalidadeFrete modalidade = buscarModalidadeFrete(nomeModalidadeDesejada);

                // 4. Lógica de Preço Sugerido (Mercado)
                BigDecimal precoSugerido = anttPisoMinimo.multiply(new BigDecimal("1.20")).setScale(2,
                                RoundingMode.HALF_UP);
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