package br.com.wta.frete.colaboradores.service;

import br.com.wta.frete.colaboradores.controller.dto.VeiculoRequest;
import br.com.wta.frete.colaboradores.controller.dto.VeiculoResponse;
import br.com.wta.frete.colaboradores.entity.Transportador;
import br.com.wta.frete.colaboradores.entity.Veiculo;
import br.com.wta.frete.colaboradores.repository.TransportadorRepository;
import br.com.wta.frete.colaboradores.repository.VeiculoRepository;
import br.com.wta.frete.colaboradores.service.mapper.VeiculoMapper;
import br.com.wta.frete.shared.exception.InvalidDataException;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela lógica de negócio da entidade Veiculo.
 */
@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final TransportadorRepository transportadorRepository;
    private final VeiculoMapper veiculoMapper;

    /**
     * Cadastra um novo veículo para um Transportador específico.
     *
     * @param request O DTO com os dados do novo veículo.
     * @return O DTO de resposta do veículo cadastrado.
     * @throws ResourceNotFoundException Se o Transportador não for encontrado.
     * @throws InvalidDataException      Se a placa ou o Renavam já estiverem
     *                                   cadastrados.
     */
    @Transactional
    public VeiculoResponse cadastrarVeiculo(VeiculoRequest request) {
        // 1. Validar e Obter o Transportador
        Transportador transportador = transportadorRepository
                .findByPessoaId(request.transportadorPessoaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transportador não encontrado com ID: " + request.transportadorPessoaId()));

        // 2. Validar Unicidade (Placa e Renavam)
        validarUnicidade(request.placa(), request.renavam());

        // 3. Mapear DTO para Entidade
        Veiculo veiculo = veiculoMapper.toEntity(request);

        // 4. Associar o Transportador à Entidade Veiculo
        // O Mapper ignora o campo 'transportador', então o setamos manualmente.
        veiculo.setTransportador(transportador);

        // 5. Salvar no Banco de Dados
        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);

        // 6. Mapear Entidade para Response DTO
        return veiculoMapper.toResponse(veiculoSalvo);
    }

    /**
     * Verifica se já existe um veículo cadastrado com a mesma placa ou renavam.
     *
     * @param placa   O número da placa.
     * @param renavam O número do Renavam.
     * @throws InvalidDataException Se a placa ou o renavam já existirem.
     */
    private void validarUnicidade(String placa, String renavam) {
        // Busca por Placa (ignora o case)
        if (veiculoRepository.findByPlaca(placa).isPresent()) {
            // CORREÇÃO DA EXCEÇÃO: Passando dois argumentos para o construtor
            throw new InvalidDataException(
                    "Veículo já cadastrado com a placa: " + placa, // Mensagem do Desenvolvedor
                    "A placa informada já está em uso." // Mensagem Amigável
            );
        }
        // Busca por Renavam (ignora o case)
        if (veiculoRepository.findByRenavam(renavam).isPresent()) {
            // CORREÇÃO DA EXCEÇÃO: Passando dois argumentos para o construtor
            throw new InvalidDataException(
                    "Veículo já cadastrado com o renavam: " + renavam, // Mensagem do Desenvolvedor
                    "O Renavam informado já está em uso." // Mensagem Amigável
            );
        }
    }
}