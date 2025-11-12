package br.com.wta.frete.clientes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.clientes.controller.dto.DetalheClienteRequest;
import br.com.wta.frete.clientes.controller.dto.DetalheClienteResponse;
import br.com.wta.frete.clientes.entity.DetalheCliente;
import br.com.wta.frete.clientes.entity.enums.TipoCliente;
import br.com.wta.frete.clientes.repository.DetalheClienteRepository;
import br.com.wta.frete.clientes.service.mapper.DetalheClienteMapper;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * Camada de serviço responsável pela lógica de negócio da entidade
 * DetalheCliente.
 * Contém os métodos CRUD e busca especializada para a tabela clientes.detalhes.
 */
@Service
@RequiredArgsConstructor
public class DetalheClienteService {

    // Dependências injetadas
    private final DetalheClienteRepository detalheClienteRepository;
    private final DetalheClienteMapper detalheClienteMapper;
    private final PessoaRepository pessoaRepository;

    // --- CREATE ---

    /**
     * Cria um novo detalhe de cliente associado a uma Pessoa existente.
     * * @param request O DTO de requisição contendo os dados do detalhe.
     * 
     * @return O DTO de resposta do detalhe de cliente criado.
     * @throws ResourceNotFoundException Se a Pessoa (core.pessoas) não for
     *                                   encontrada.
     */
    @SuppressWarnings("null")
    @Transactional
    public DetalheClienteResponse criarDetalheCliente(DetalheClienteRequest request) {

        // 1. Validação de Dependência: A Pessoa (pai) deve existir antes de criar o
        // detalhe 1:1.
        if (!pessoaRepository.existsById(request.pessoaId())) {
            throw new ResourceNotFoundException(
                    "Pessoa com ID %d não encontrada para criar o detalhe de cliente.".formatted(request.pessoaId()),
                    "PESSOA_NAO_ENCONTRADA");
        }

        // 2. Conversão DTO para Entidade e Persistência
        DetalheCliente detalheCliente = detalheClienteMapper.toEntity(request);
        DetalheCliente savedEntity = detalheClienteRepository.save(detalheCliente);

        // 3. Conversão Entidade para DTO de Resposta
        return detalheClienteMapper.toResponse(savedEntity);
    }

    // --- READ ---

    /**
     * Busca um detalhe de cliente pelo seu ID (que é o mesmo ID da Pessoa).
     * * @param pessoaId O ID da Pessoa/DetalheCliente.
     * 
     * @return O DTO de resposta do detalhe de cliente.
     * @throws ResourceNotFoundException Se o detalhe de cliente não for encontrado.
     */
    @Transactional(readOnly = true)
    public DetalheClienteResponse buscarDetalheClientePorId(Long pessoaId) {
        @SuppressWarnings("null")
        DetalheCliente entity = detalheClienteRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Detalhe de Cliente com ID %d não encontrado.".formatted(pessoaId),
                        "DETALHE_CLIENTE_NAO_ENCONTRADO"));

        return detalheClienteMapper.toResponse(entity);
    }

    /**
     * Busca todos os detalhes de clientes.
     * * @return Uma lista de DTOs de resposta.
     */
    @Transactional(readOnly = true)
    public List<DetalheClienteResponse> buscarTodosDetalhesClientes() {
        return detalheClienteRepository.findAll().stream()
                .map(detalheClienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca clientes com base no TipoCliente (e.g., PESSOA_FISICA).
     * O método usa o ENUM nativo, conforme corrigido no Repositório.
     * * @param tipoCliente O ENUM do tipo de cliente a ser buscado.
     * 
     * @return Uma lista de DTOs de resposta.
     */
    @Transactional(readOnly = true)
    public List<DetalheClienteResponse> buscarClientesPorTipo(TipoCliente tipoCliente) {
        return detalheClienteRepository.findByTipoCliente(tipoCliente).stream()
                .map(detalheClienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- UPDATE ---

    /**
     * Atualiza os detalhes de um cliente existente.
     * * @param pessoaId O ID da Pessoa/DetalheCliente a ser atualizado.
     * 
     * @param request Os novos dados do detalhe de cliente.
     * @return O DTO de resposta do detalhe de cliente atualizado.
     * @throws ResourceNotFoundException Se o detalhe de cliente não for encontrado.
     */
    @Transactional
    public DetalheClienteResponse atualizarDetalheCliente(Long pessoaId, DetalheClienteRequest request) {

        // 1. Busca e Validação da Entidade
        @SuppressWarnings("null")
        DetalheCliente existingEntity = detalheClienteRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Detalhe de Cliente com ID %d não encontrado para atualização.".formatted(pessoaId),
                        "DETALHE_CLIENTE_NAO_ENCONTRADO"));

        // 2. Mapeamento dos novos dados (Preservamos o 'pessoa' e 'pessoaId'
        // existentes)
        // Usamos o mapeamento do DTO para a Entidade para transferir os dados,
        // mas precisamos de cuidado para não sobrescrever a Pessoa de referência,
        // embora o MapStruct trate isso via @Mapping(target = "pessoaId", ignore =
        // true).
        // Para simplificar e garantir, fazemos a atualização manual dos campos:

        // Para usar o método toEntity, precisariamos do PessoaMapper para construir a
        // Pessoa,
        // ou criar um método específico de update no MapStruct. Vamos fazer a
        // atualização
        // dos campos diretamente para ser mais explícito e seguro no Service:

        TipoCliente novoTipoCliente = detalheClienteMapper.map(request.tipoCliente());

        existingEntity.setTipoCliente(novoTipoCliente);
        existingEntity.setPreferenciasColeta(request.preferenciasColeta());

        // 3. Persistência
        DetalheCliente updatedEntity = detalheClienteRepository.save(existingEntity);

        // 4. Conversão e Retorno
        return detalheClienteMapper.toResponse(updatedEntity);
    }

    // --- DELETE ---

    /**
     * Exclui um detalhe de cliente pelo seu ID.
     * * @param pessoaId O ID da Pessoa/DetalheCliente a ser excluído.
     * 
     * @throws ResourceNotFoundException Se o detalhe de cliente não for encontrado.
     */
    @SuppressWarnings("null")
    @Transactional
    public void excluirDetalheCliente(Long pessoaId) {

        if (!detalheClienteRepository.existsById(pessoaId)) {
            throw new ResourceNotFoundException(
                    "Detalhe de Cliente com ID %d não encontrado para exclusão.".formatted(pessoaId),
                    "DETALHE_CLIENTE_NAO_ENCONTRADO");
        }

        // Como o relacionamento é 1:1 com CASCADE no banco, excluir a PessoaId
        // em DetalheCliente não excluirá a Pessoa em core.pessoas (a menos que a FK
        // na tabela 'clientes.detalhes' tenha DELETE CASCADE para core.pessoas, o que é
        // o caso).
        // Mas a exclusão deve ser segura aqui.
        detalheClienteRepository.deleteById(pessoaId);
    }
}