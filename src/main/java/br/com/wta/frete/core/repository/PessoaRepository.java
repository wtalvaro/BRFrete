package br.com.wta.frete.core.repository;

import br.com.wta.frete.core.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Documentação: Repositório JPA para a entidade Pessoa.
 * Estende JpaRepository para herdar operações CRUD e consulta avançadas.
 * Localizado no módulo 'core'.
 */
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    /**
     * Documentação: Método crucial para a Lógica de Cadastro (Etapa de Validação).
     * Verifica se já existe um usuário com o email fornecido, garantindo a
     * unicidade.
     * 
     * @param email Email do usuário
     * @return Optional<Pessoa> Retorna a Pessoa se encontrar, ou Optional vazio.
     */
    Optional<Pessoa> findByEmail(String email);

    /**
     * Documentação: Método crucial para a Lógica de Cadastro (Etapa de Validação).
     * Verifica se já existe um usuário com o documento (CPF/CNPJ) fornecido,
     * garantindo a unicidade.
     * 
     * @param documento Documento (CPF/CNPJ) do usuário
     * @return Optional<Pessoa> Retorna a Pessoa se encontrar, ou Optional vazio.
     */
    Optional<Pessoa> findByDocumento(String documento);

    /**
     * Documentação: NOVO MÉTODO para procurar uma Pessoa usando o ID do Provedor
     * Social.
     * Essencial para a lógica de Login Social, permitindo encontrar utilizadores
     * que se registaram via Google, Facebook, etc.
     * 
     * @param socialId O ID único do utilizador fornecido pelo provedor OAuth2
     *                 (e.g., o campo 'sub' do Google).
     * @return Optional<Pessoa> Retorna a Pessoa se encontrar, ou Optional vazio.
     */
    Optional<Pessoa> findBySocialId(String socialId); // <--- IMPLEMENTAÇÃO NECESSÁRIA

    // O Spring Data JPA gerará a implementação automaticamente em tempo de
    // execução.
}