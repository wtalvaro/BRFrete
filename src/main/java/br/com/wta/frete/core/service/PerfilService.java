package br.com.wta.frete.core.service;

import br.com.wta.frete.core.entity.Perfil;
import br.com.wta.frete.core.repository.PerfilRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de Serviço para a entidade Perfil (core.perfis).
 * Contém as operações CRUD e lógicas de negócio para gerenciar perfis de
 * acesso.
 * A chave primária é Integer.
 */
@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    // =========================================================================
    // C - CREATE (Criar)
    // =========================================================================

    /**
     * Salva um novo Perfil.
     * 
     * @param perfil O objeto Perfil a ser salvo.
     * @return O Perfil salvo.
     */
    @SuppressWarnings("null")
    @Transactional
    public Perfil salvar(Perfil perfil) {
        // Regras de negócio podem ser adicionadas aqui (ex: normalização do nome)
        return perfilRepository.save(perfil);
    }

    // =========================================================================
    // R - READ (Ler/Buscar)
    // =========================================================================

    /**
     * Busca um Perfil pelo ID (Chave Primária).
     *
     * @param perfilId O ID do Perfil (Integer).
     * @return O Perfil encontrado.
     * @throws ResourceNotFoundException Se o perfil não for encontrado.
     */
    @SuppressWarnings("null")
    public Perfil buscarPorId(Integer perfilId) {
        return perfilRepository.findById(perfilId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Perfil não encontrado para o ID: '%d'", perfilId),
                        "PERFIL_NAO_ENCONTRADO_ID"));
    }

    /**
     * Busca um Perfil pelo nome único (e.g., "ADMIN", "CLIENTE").
     *
     * @param nomePerfil O nome do Perfil.
     * @return O Perfil encontrado.
     * @throws ResourceNotFoundException Se o perfil não for encontrado.
     */
    public Perfil buscarPorNomePerfil(String nomePerfil) {
        return perfilRepository.findByNomePerfil(nomePerfil) // Método disponível no Repositório
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Perfil não encontrado para o nome: '%s'", nomePerfil),
                        "PERFIL_NAO_ENCONTRADO_NOME"));
    }

    /**
     * Retorna todos os Perfis cadastrados.
     *
     * @return Uma lista de objetos Perfil.
     */
    public List<Perfil> listarTodos() {
        return perfilRepository.findAll();
    }

    // =========================================================================
    // U - UPDATE (Atualizar)
    // =========================================================================

    /**
     * Atualiza a descrição e/ou nome de um perfil existente.
     *
     * @param perfilId       O ID do Perfil a ser atualizado.
     * @param perfilDetalhes O objeto Perfil com os novos detalhes.
     * @return O Perfil atualizado.
     * @throws ResourceNotFoundException Se o perfil com o ID especificado não for
     *                                   encontrado.
     */
    @Transactional
    public Perfil atualizar(Integer perfilId, Perfil perfilDetalhes) {
        Perfil perfilExistente = buscarPorId(perfilId);

        // Atualiza apenas os campos permitidos
        perfilExistente.setNomePerfil(perfilDetalhes.getNomePerfil());
        perfilExistente.setDescricao(perfilDetalhes.getDescricao());

        return perfilRepository.save(perfilExistente);
    }

    // =========================================================================
    // D - DELETE (Deletar)
    // =========================================================================

    /**
     * Deleta um Perfil pelo ID.
     *
     * @param perfilId O ID do Perfil a ser deletado.
     * @throws ResourceNotFoundException Se o perfil não for encontrado.
     */
    @SuppressWarnings("null")
    @Transactional
    public void deletar(Integer perfilId) {
        // Garantimos que o perfil existe antes de deletar
        Perfil perfilExistente = buscarPorId(perfilId);

        // A deleção falhará automaticamente (violando Foreign Key) se o perfil estiver
        // em uso.
        perfilRepository.delete(perfilExistente);
    }
}