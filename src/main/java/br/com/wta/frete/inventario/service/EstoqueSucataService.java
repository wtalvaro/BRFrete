package br.com.wta.frete.inventario.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.colaboradores.entity.Sucateiro;
import br.com.wta.frete.colaboradores.repository.SucateiroRepository;
import br.com.wta.frete.inventario.controller.dto.EstoqueSucataRequest;
import br.com.wta.frete.inventario.controller.dto.EstoqueSucataResponse;
import br.com.wta.frete.inventario.entity.EstoqueSucata;
import br.com.wta.frete.inventario.repository.EstoqueSucataRepository;
import br.com.wta.frete.inventario.service.mapper.EstoqueSucataMapper;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

@Service
public class EstoqueSucataService {

	private final EstoqueSucataRepository estoqueSucataRepository;
	private final EstoqueSucataMapper estoqueSucataMapper;
	private final SucateiroRepository sucateiroRepository;

	// Construtor com injeção de dependências
	public EstoqueSucataService(EstoqueSucataRepository estoqueSucataRepository,
			EstoqueSucataMapper estoqueSucataMapper, SucateiroRepository sucateiroRepository) {
		this.estoqueSucataRepository = estoqueSucataRepository;
		this.estoqueSucataMapper = estoqueSucataMapper;
		this.sucateiroRepository = sucateiroRepository;
	}

	/**
	 * Documentação: Cria um novo registro de estoque ou atualiza um existente
	 * (lógica idempotente).
	 * Garante a unicidade do par (Sucateiro, TipoMaterial).
	 *
	 * @param request O DTO de requisição com os dados do estoque.
	 * @return O DTO de resposta do item de estoque criado/atualizado.
	 * @throws ResourceNotFoundException Se o Sucateiro não for encontrado.
	 */
	@Transactional
	public EstoqueSucataResponse criarOuAtualizarEstoqueSucata(EstoqueSucataRequest request) {

		// 1. Validação: Encontra o Sucateiro, garantindo que ele exista
		Sucateiro sucateiro = sucateiroRepository.findByPessoaId(request.sucateiroPessoaId())
				.orElseThrow(() -> new ResourceNotFoundException("Sucateiro com ID " + request.sucateiroPessoaId() + " não encontrado."));

		// 2. Lógica Idempotente: Tenta encontrar um item de estoque para este Sucateiro e TipoMaterial
		Optional<EstoqueSucata> estoqueExistente = estoqueSucataRepository
				.findBySucateiroPessoaIdAndTipoMaterial(request.sucateiroPessoaId(), request.tipoMaterial());

		EstoqueSucata estoque;

		if (estoqueExistente.isPresent()) {
			// Caminho de ATUALIZAÇÃO
			estoque = estoqueExistente.get();
			// Atualiza apenas os campos mapeáveis do Request (quantidade, qualidade, localizacao)
			estoqueSucataMapper.updateEntityFromRequest(request, estoque);
			// Força a atualização do timestamp
			estoque.setDataAtualizacao(ZonedDateTime.now());
		} else {
			// Caminho de CRIAÇÃO
			estoque = estoqueSucataMapper.toEntity(request);
			// Associa a Entidade Sucateiro (Relacionamento FK)
			estoque.setSucateiro(sucateiro);
			// O dataAtualizacao já é setado por padrão na Entidade.
		}

		// 3. Persistência e Mapeamento
		EstoqueSucata estoqueSalvo = estoqueSucataRepository.save(estoque);

		return estoqueSucataMapper.toResponse(estoqueSalvo);
	}
	
	/**
	 * Documentação: Busca um item de estoque por ID.
	 *
	 * @param estoqueId O ID do item de estoque.
	 * @return O DTO de resposta do item de estoque.
	 * @throws ResourceNotFoundException Se o item de estoque não for encontrado.
	 */
	@SuppressWarnings("null")
    @Transactional(readOnly = true)
	public EstoqueSucataResponse buscarPorId(Long estoqueId) {
		EstoqueSucata estoque = estoqueSucataRepository.findById(estoqueId)
				.orElseThrow(() -> new ResourceNotFoundException("Estoque de Sucata com ID " + estoqueId + " não encontrado."));
		
		return estoqueSucataMapper.toResponse(estoque);
	}

	/**
	 * Documentação: Lista todos os itens de estoque de um Sucateiro específico.
	 *
	 * @param sucateiroPessoaId O ID da Pessoa/Sucateiro.
	 * @return Uma lista de DTOs de resposta do estoque.
	 */
	@SuppressWarnings("null")
    @Transactional(readOnly = true)
	public List<EstoqueSucataResponse> listarEstoquePorSucateiro(Long sucateiroPessoaId) {
		// Validação opcional: verifica se o Sucateiro existe
		if (!sucateiroRepository.existsById(sucateiroPessoaId)) {
			throw new ResourceNotFoundException("Sucateiro com ID " + sucateiroPessoaId + " não encontrado.");
		}
		
		List<EstoqueSucata> estoque = estoqueSucataRepository.findBySucateiroPessoaId(sucateiroPessoaId);
		
		return estoque.stream()
				.map(estoqueSucataMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	/**
	 * Documentação: Deleta um item de estoque pelo ID.
	 *
	 * @param estoqueId O ID do item de estoque a ser deletado.
	 * @throws ResourceNotFoundException Se o item de estoque não for encontrado.
	 */
	@SuppressWarnings("null")
    @Transactional
	public void deletarEstoque(Long estoqueId) {
		if (!estoqueSucataRepository.existsById(estoqueId)) {
			throw new ResourceNotFoundException("Estoque de Sucata com ID " + estoqueId + " não encontrado para exclusão.");
		}
		estoqueSucataRepository.deleteById(estoqueId);
	}
}