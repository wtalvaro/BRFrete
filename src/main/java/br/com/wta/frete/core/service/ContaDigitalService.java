package br.com.wta.frete.core.service;

import br.com.wta.frete.core.entity.ContaDigital;
import br.com.wta.frete.core.entity.enums.StatusKYC;
import br.com.wta.frete.core.repository.ContaDigitalRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException; // Classe de exceção assumida
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * Camada de Serviço para a entidade ContaDigital (Wallet).
 * Contém a lógica de negócio e as operações CRUD para gerenciar contas
 * digitais.
 */
@Service
public class ContaDigitalService {

    private final ContaDigitalRepository contaDigitalRepository;

    // Injeção de dependência via construtor
    public ContaDigitalService(ContaDigitalRepository contaDigitalRepository) {
        this.contaDigitalRepository = contaDigitalRepository;
    }

    // =========================================================================
    // C - CREATE (Criar)
    // =========================================================================

    /**
     * Abre a conta digital (Wallet) para a Pessoa recém-cadastrada.
     * Necessário para todos os colaboradores que recebem pagamentos.
     * * @param pessoaId O ID da pessoa (chave primária/estrangeira).
     * 
     * @return A entidade ContaDigital criada.
     */
    @Transactional
    public ContaDigital abrirNovaConta(Long pessoaId) {

        // 1. Geração de um ID de Conta Único (UUID)
        String contaUuid = UUID.randomUUID().toString(); // Mapeia para conta_uuid

        // 2. Criação da Entidade
        ContaDigital conta = new ContaDigital();
        conta.setPessoaId(pessoaId);
        conta.setContaUuid(contaUuid);
        // Status inicial de verificação de identidade (Know Your Customer) é PENDENTE
        conta.setStatusKyc(StatusKYC.PENDENTE);

        // 3. Persistência
        return contaDigitalRepository.save(conta);
    }

    // =========================================================================
    // R - READ (Ler/Buscar)
    // =========================================================================

    /**
     * Busca uma Conta Digital pelo ID da Pessoa (Chave Primária).
     *
     * @param pessoaId O ID da Pessoa que também é o ID da Conta Digital.
     * @return A ContaDigital encontrada.
     * @throws ResourceNotFoundException Se a conta não for encontrada.
     */
    @SuppressWarnings("null")
    public ContaDigital buscarPorId(Long pessoaId) {
        // CORREÇÃO: Construtor da ResourceNotFoundException ajustado para 2 argumentos
        return contaDigitalRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("ContaDigital não encontrada para o pessoaId: '%d'", pessoaId),
                        "CONTA_DIGITAL_NAO_ENCONTRADA" // Código de erro customizado
                ));
    }

    /**
     * Busca uma Conta Digital pelo seu identificador único (UUID).
     *
     * @param contaUuid O UUID da conta.
     * @return A ContaDigital encontrada, ou null se não for encontrada.
     */
    public ContaDigital buscarPorContaUuid(String contaUuid) {
        // Utiliza o método customizado no ContaDigitalRepository
        return contaDigitalRepository.findByContaUuid(contaUuid);
    }

    /**
     * Retorna todas as Contas Digitais cadastradas.
     *
     * @return Uma lista de objetos ContaDigital.
     */
    public List<ContaDigital> listarTodas() {
        return contaDigitalRepository.findAll();
    }

    // =========================================================================
    // U - UPDATE (Atualizar)
    // =========================================================================

    /**
     * Atualiza o Status KYC de uma conta digital.
     * * @param pessoaId O ID da Pessoa/Conta a ser atualizada.
     * 
     * @param novoStatus O novo status de KYC.
     * @return A ContaDigital atualizada.
     * @throws ResourceNotFoundException Se a conta com o ID especificado não for
     *                                   encontrada.
     */
    @Transactional
    public ContaDigital atualizarStatusKyc(Long pessoaId, StatusKYC novoStatus) {
        // 1. Busca e verifica se a conta existe
        ContaDigital contaExistente = buscarPorId(pessoaId);

        // 2. Aplica a mudança
        contaExistente.setStatusKyc(novoStatus);

        // 3. Persiste a mudança
        return contaDigitalRepository.save(contaExistente);
    }

    // =========================================================================
    // D - DELETE (Deletar)
    // =========================================================================

    /**
     * Deleta uma Conta Digital pelo ID da Pessoa (Chave Primária).
     *
     * @param pessoaId O ID da Pessoa/Conta a ser deletada.
     * @throws ResourceNotFoundException Se a conta não for encontrada.
     */
    @SuppressWarnings("null")
    @Transactional
    public void deletar(Long pessoaId) {
        // Busca primeiro para garantir que a exceção de "não encontrado" seja lançada
        // se necessário
        ContaDigital contaExistente = buscarPorId(pessoaId);
        contaDigitalRepository.delete(contaExistente);
    }
}