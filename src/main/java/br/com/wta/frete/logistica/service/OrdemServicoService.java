package br.com.wta.frete.logistica.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.logistica.controller.dto.OrdemServicoRequest;
import br.com.wta.frete.logistica.controller.dto.OrdemServicoResponse;
import br.com.wta.frete.logistica.entity.OrdemServico;
import br.com.wta.frete.logistica.repository.OrdemServicoRepository;
import br.com.wta.frete.logistica.service.mapper.OrdemServicoMapper;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela lógica de negócio das Ordens de Serviço (CRUD,
 * validações, etc.).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrdemServicoService {

    // Dependências injetadas pelo Spring
    private final OrdemServicoRepository ordemServicoRepository;
    private final OrdemServicoMapper ordemServicoMapper;

    // --- Métodos Requeridos pelo Controller (Implementação Mínima para Compilação)
    // ---

    /**
     * Cria uma nova Ordem de Serviço, buscando as entidades relacionadas e
     * persistindo no banco.
     */
    public OrdemServicoResponse criarOrdemServico(OrdemServicoRequest request) {
        // 1. Converte o DTO de Request para a Entidade
        OrdemServico novaOrdem = ordemServicoMapper.toEntity(request);

        // 2. Lógica de Negócio Pendente:
        // * Buscar DetalheCliente e Transportador (via service/repository) e associar à
        // 'novaOrdem'.
        // * Calcular Distância/Frete (se necessário).
        // * Definir o Status inicial (já é PENDENTE por padrão no construtor da
        // Entidade).

        // 3. Persiste a Entidade
        @SuppressWarnings("null")
        OrdemServico savedOrdem = ordemServicoRepository.save(novaOrdem);

        // 4. Retorna o DTO de Response
        return ordemServicoMapper.toResponse(savedOrdem);
    }

    /**
     * Busca uma Ordem de Serviço por ID.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public OrdemServicoResponse buscarPorId(Long id) {
        // 1. Busca a entidade, lançando exceção se não for encontrada
        OrdemServico ordem = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada com ID: " + id));

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