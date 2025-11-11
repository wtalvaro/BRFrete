package br.com.wta.frete.marketplace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.marketplace.controller.dto.CategoriaRequest;
import br.com.wta.frete.marketplace.controller.dto.CategoriaResponse;
import br.com.wta.frete.marketplace.entity.Categoria;
import br.com.wta.frete.marketplace.repository.CategoriaRepository;
import br.com.wta.frete.marketplace.service.mapper.CategoriaMapper;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

/**
 * Service dedicado à gestão completa da entidade Categoria
 * (marketplace.categorias).
 * Oferece métodos CRUD para manipulação de categorias.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    /**
     * Documentação: Cria uma nova categoria com base no DTO de requisição.
     * * @param request O DTO com os dados da nova categoria.
     * 
     * @return O DTO de resposta da categoria criada.
     */
    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        // 1. Converte o DTO para a Entidade
        Categoria categoria = categoriaMapper.toEntity(request);

        // 2. Salva no banco de dados
        @SuppressWarnings("null")
        Categoria categoriaSalva = categoriaRepository.save(categoria);

        // 3. Converte a Entidade salva para o DTO de Resposta
        return categoriaMapper.toResponse(categoriaSalva);
    }

    /**
     * Documentação: Busca uma categoria pelo seu ID.
     * * @param id O ID da categoria.
     * 
     * @return O DTO de resposta da categoria.
     * @throws ResourceNotFoundException se a categoria não for encontrada.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));

        return categoriaMapper.toResponse(categoria);
    }

    /**
     * Documentação: Retorna todas as categorias cadastradas.
     * * @return Uma lista de DTOs de CategoriaResponse.
     */
    @Transactional(readOnly = true)
    public List<CategoriaResponse> buscarTodos() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Documentação: Atualiza uma categoria existente.
     * * @param id O ID da categoria a ser atualizada.
     * 
     * @param request O DTO com os novos dados.
     * @return O DTO de resposta da categoria atualizada.
     * @throws ResourceNotFoundException se a categoria não for encontrada.
     */
    @SuppressWarnings("null")
    @Transactional
    public CategoriaResponse atualizar(Integer id, CategoriaRequest request) {
        // 1. Busca a categoria existente
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));

        // 2. Atualiza os campos da entidade com os dados do request (usando MapStruct
        // @MappingTarget)
        categoriaMapper.updateEntity(request, categoria);

        // 3. Salva a entidade atualizada
        Categoria categoriaAtualizada = categoriaRepository.save(categoria);

        // 4. Retorna o DTO
        return categoriaMapper.toResponse(categoriaAtualizada);
    }

    /**
     * Documentação: Remove uma categoria pelo seu ID.
     * * @param id O ID da categoria a ser deletada.
     * 
     * @throws ResourceNotFoundException se a categoria não for encontrada.
     */
    @SuppressWarnings("null")
    @Transactional
    public void deletar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada com ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    /**
     * Método Auxiliar: Busca a Entidade Categoria.
     * Útil para ser injetado e usado por outros Services (como ProdutoService).
     *
     * @param id O ID da categoria.
     * @return A Entidade Categoria.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public Categoria buscarEntidadePorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));
    }
}