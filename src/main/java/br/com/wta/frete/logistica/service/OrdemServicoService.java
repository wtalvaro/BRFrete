package br.com.wta.frete.logistica.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.clientes.entity.DetalheCliente;
import br.com.wta.frete.clientes.repository.DetalheClienteRepository;
import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.colaboradores.repository.TransportadorRepository;
import br.com.wta.frete.core.entity.enums.StatusServico;
import br.com.wta.frete.logistica.controller.dto.OrdemServicoRequest;
import br.com.wta.frete.logistica.controller.dto.OrdemServicoResponse;
import br.com.wta.frete.logistica.entity.OrdemServico;
import br.com.wta.frete.logistica.repository.OrdemServicoRepository;
import br.com.wta.frete.logistica.service.mapper.OrdemServicoMapper;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável por gerenciar o ciclo de vida da Ordem de Serviço (OS).
 * Orquestra a criação da OS e o subsequente processo de precificação do Frete.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrdemServicoService {

    // Dependências injetadas pelo Spring
    private final OrdemServicoRepository ordemServicoRepository;
    private final OrdemServicoMapper ordemServicoMapper;

    // Dependências para a orquestração (Ponto 2)
    private final FreteService freteService;
    private final DetalheClienteRepository detalheClienteRepository;
    private final TransportadorRepository transportadorRepository;

    /**
     * FLUXO CENTRAL: Cria uma nova Ordem de Serviço e, em seguida, inicia o
     * processo
     * de criação e precificação do Frete, usando a modalidade desejada do Request.
     * <p>
     * Todo o processo ocorre sob uma única transação {@code @Transactional}.
     * </p>
     *
     * @param request O DTO de requisição da OS, incluindo o nome da Modalidade de
     *                Frete.
     * @return O DTO de resposta da Ordem de Serviço criada.
     * @throws ResourceNotFoundException Se o Cliente ou Transportador não forem
     *                                   encontrados.
     */
    @SuppressWarnings("null")
    public OrdemServicoResponse criarOrdemServico(OrdemServicoRequest request) {

        // 1. BUSCA E ASSOCIAÇÃO DE ENTIDADES DE RELACIONAMENTO

        // Cliente Solicitante é obrigatório (usa id, que é a PK)
        DetalheCliente cliente = detalheClienteRepository.findById(request.clienteSolicitanteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente Solicitante não encontrado com ID: " + request.clienteSolicitanteId()));

        // Transportador Designado (Opcional)
        Transportador transportador = null;
        if (request.transportadorDesignadoId() != null) {
            transportador = transportadorRepository.findById(request.transportadorDesignadoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Transportador Designado não encontrado com ID: " + request.transportadorDesignadoId()));
        }

        // 2. MAPEAMENTO E CONFIGURAÇÃO DA ENTIDADE OS
        OrdemServico ordemServico = ordemServicoMapper.toEntity(request);
        ordemServico.setClienteSolicitante(cliente);
        ordemServico.setTransportadorDesignado(transportador); // Pode ser null
        ordemServico.setStatus(StatusServico.PENDENTE); // Status inicial definido

        // 3. PERSISTÊNCIA DA ORDEM DE SERVIÇO
        OrdemServico ordemSalva = ordemServicoRepository.save(ordemServico);

        // 4. ORQUESTRAÇÃO: CRIAÇÃO DO FRETE (Usa o FreteService refatorado)
        freteService.criarFrete(
                ordemSalva,
                request.itensFrete(),
                request.nomeModalidadeFrete() // O CAMPO FLEXÍVEL É PASSADO AQUI
        );

        // 5. RETORNO
        return ordemServicoMapper.toResponse(ordemSalva);
    }

    /**
     * Busca uma Ordem de Serviço por ID.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public OrdemServicoResponse buscarPorId(Long id) {
        // Usa ResourceNotFoundException para consistência e clareza
        OrdemServico ordem = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada com ID: " + id));

        // 2. Converte a Entidade para Response DTO
        return ordemServicoMapper.toResponse(ordem);
    }

    /**
     * Busca todas as Ordens de Serviço.
     */
    @Transactional(readOnly = true)
    public List<OrdemServicoResponse> buscarTodas() {
        // 1. Busca todas as entidades
        List<OrdemServico> ordens = ordemServicoRepository.findAll();

        // 2. Converte a lista de Entidades para uma lista de Response DTOs
        return ordens.stream()
                .map(ordemServicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Método de exemplo.
     */
    public String status() {
        return "OrdemServicoService está operacional.";
    }
}