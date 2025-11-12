package br.com.wta.frete.clientes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.clientes.controller.dto.PedidoColetaRequest;
import br.com.wta.frete.clientes.controller.dto.PedidoColetaResponse;
import br.com.wta.frete.clientes.entity.PedidoColeta;
import br.com.wta.frete.clientes.repository.DetalheClienteRepository;
import br.com.wta.frete.clientes.repository.PedidoColetaRepository;
import br.com.wta.frete.clientes.service.mapper.PedidoColetaMapper;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Camada de serviço responsável pela lógica de negócio da entidade
 * PedidoColeta.
 * Garante o ciclo de vida completo (CRUD) dos pedidos de coleta de clientes.
 */
@Service
@RequiredArgsConstructor
public class PedidoColetaService {

    // Dependências injetadas
    private final PedidoColetaRepository pedidoColetaRepository;
    private final PedidoColetaMapper pedidoColetaMapper;
    private final DetalheClienteRepository detalheClienteRepository; // Necessário para validação da FK

    // --- CREATE ---

    /**
     * Cria um novo pedido de coleta.
     * 
     * @param request O DTO de requisição contendo os dados do pedido.
     * @return O DTO de resposta do pedido criado.
     * @throws ResourceNotFoundException Se o Cliente (clientes.detalhes) não for
     *                                   encontrado.
     */
    @SuppressWarnings("null")
    @Transactional
    public PedidoColetaResponse criarPedidoColeta(PedidoColetaRequest request) {

        // 1. Validação de Dependência: O cliente deve existir.
        // O PedidoColeta.cliente_id (FK) referencia clientes.detalhes.pessoa_id.
        if (!detalheClienteRepository.existsById(request.clientePessoaId())) {
            throw new ResourceNotFoundException(
                    "Cliente com ID %d não encontrado para criar o pedido de coleta."
                            .formatted(request.clientePessoaId()),
                    "CLIENTE_NAO_ENCONTRADO");
        }

        // 2. Conversão DTO para Entidade
        PedidoColeta pedidoColeta = pedidoColetaMapper.toEntity(request);

        // 3. Persistência da Entidade
        PedidoColeta savedEntity = pedidoColetaRepository.save(pedidoColeta);

        // 4. Conversão Entidade para DTO de Resposta
        return pedidoColetaMapper.toResponse(savedEntity);
    }

    // --- READ ---

    /**
     * Busca um pedido de coleta pelo seu ID.
     * 
     * @param pedidoId O ID do pedido.
     * @return O DTO de resposta do pedido.
     * @throws ResourceNotFoundException Se o pedido de coleta não for encontrado.
     */
    @Transactional(readOnly = true)
    public PedidoColetaResponse buscarPedidoColetaPorId(Integer pedidoId) {
        @SuppressWarnings("null")
        PedidoColeta entity = pedidoColetaRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido de Coleta com ID %d não encontrado.".formatted(pedidoId),
                        "PEDIDO_COLETA_NAO_ENCONTRADO"));

        return pedidoColetaMapper.toResponse(entity);
    }

    /**
     * Busca todos os pedidos de coleta feitos por um cliente específico.
     * Utiliza o método de busca por relacionamento (FK) do repositório.
     * 
     * @param clientePessoaId O ID da Pessoa/Cliente.
     * @return Uma lista de DTOs de resposta.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public List<PedidoColetaResponse> buscarPedidosPorCliente(Long clientePessoaId) {
        // Não é estritamente necessário validar a existência do cliente, mas é uma boa
        // prática
        if (!detalheClienteRepository.existsById(clientePessoaId)) {
            throw new ResourceNotFoundException("Cliente com ID %d não encontrado.".formatted(clientePessoaId),
                    "CLIENTE_NAO_ENCONTRADO");
        }

        // Utiliza o método de consulta do Spring Data JPA que navega pelo
        // relacionamento
        return pedidoColetaRepository.findByClientePessoaId(clientePessoaId).stream()
                .map(pedidoColetaMapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- UPDATE ---

    /**
     * Atualiza um pedido de coleta existente.
     * 
     * @param pedidoId O ID do pedido a ser atualizado.
     * @param request  Os novos dados do pedido (DTO).
     * @return O DTO de resposta do pedido atualizado.
     * @throws ResourceNotFoundException Se o pedido de coleta não for encontrado.
     */
    @Transactional
    public PedidoColetaResponse atualizarPedidoColeta(Integer pedidoId, PedidoColetaRequest request) {

        // 1. Busca e Validação da Entidade
        @SuppressWarnings("null")
        PedidoColeta existingEntity = pedidoColetaRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido de Coleta com ID %d não encontrado para atualização.".formatted(pedidoId),
                        "PEDIDO_COLETA_NAO_ENCONTRADO"));

        // 2. Utiliza o método de atualização do Mapper (updateEntity)
        pedidoColetaMapper.updateEntity(request, existingEntity);

        // 3. Persistência (Merge)
        @SuppressWarnings("null")
        PedidoColeta updatedEntity = pedidoColetaRepository.save(existingEntity);

        // 4. Conversão e Retorno
        return pedidoColetaMapper.toResponse(updatedEntity);
    }

    // --- DELETE ---

    /**
     * Exclui um pedido de coleta pelo seu ID.
     * 
     * @param pedidoId O ID do pedido a ser excluído.
     * @throws ResourceNotFoundException Se o pedido de coleta não for encontrado.
     */
    @SuppressWarnings("null")
    @Transactional
    public void excluirPedidoColeta(Integer pedidoId) {

        if (!pedidoColetaRepository.existsById(pedidoId)) {
            throw new ResourceNotFoundException(
                    "Pedido de Coleta com ID %d não encontrado para exclusão.".formatted(pedidoId),
                    "PEDIDO_COLETA_NAO_ENCONTRADO");
        }

        pedidoColetaRepository.deleteById(pedidoId);
    }
}