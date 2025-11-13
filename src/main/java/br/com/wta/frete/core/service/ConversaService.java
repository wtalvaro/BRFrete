package br.com.wta.frete.core.service;

import br.com.wta.frete.core.entity.Conversa;
import br.com.wta.frete.core.entity.enums.TipoConversa;
import br.com.wta.frete.core.repository.ConversaRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de Serviço para a entidade Conversa (core.conversas).
 * Contém a lógica de negócio e as operações CRUD básicas para gerenciar chats.
 */
@Service
public class ConversaService {

    private final ConversaRepository conversaRepository;

    /**
     * Injeção de dependência via construtor.
     */
    public ConversaService(ConversaRepository conversaRepository) {
        this.conversaRepository = conversaRepository;
    }

    // =========================================================================
    // C - CREATE (Criar)
    // =========================================================================

    /**
     * Inicia uma nova Conversa com um Tipo específico.
     * Esta é a operação de criação primária.
     *
     * @param tipoConversa O tipo da conversa (ex: PRIVADA, GRUPO).
     * @return A entidade Conversa salva.
     */
    @Transactional
    public Conversa iniciarNovaConversa(TipoConversa tipoConversa) {
        Conversa conversa = new Conversa();
        conversa.setTipoConversa(tipoConversa);

        // dataCriacao é definida automaticamente no construtor da entidade
        // ultimaMensagemEm será null até a primeira mensagem ser enviada

        return conversaRepository.save(conversa);
    }

    // =========================================================================
    // R - READ (Ler/Buscar)
    // =========================================================================

    /**
     * Busca uma Conversa pelo ID (Chave Primária).
     *
     * @param conversaId O ID da Conversa.
     * @return A Conversa encontrada.
     * @throws ResourceNotFoundException Se a conversa não for encontrada.
     */
    @SuppressWarnings("null")
    public Conversa buscarPorId(Long conversaId) {
        // Busca no repositório e lança exceção customizada se não encontrar.
        return conversaRepository.findById(conversaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Conversa não encontrada para o ID: '%d'", conversaId),
                        "CONVERSA_NAO_ENCONTRADA_ID"));
    }

    /**
     * Retorna todas as Conversas cadastradas.
     *
     * @return Uma lista de objetos Conversa.
     */
    public List<Conversa> listarTodas() {
        return conversaRepository.findAll();
    }

    // =========================================================================
    // U - UPDATE (Atualizar)
    // =========================================================================

    /**
     * Método utilitário para atualizar o campo 'ultimaMensagemEm'.
     * Geralmente chamado por um serviço de mensagens após o envio bem-sucedido.
     *
     * @param conversaId O ID da conversa a ser atualizada.
     * @param dataHora   A data e hora da última mensagem enviada.
     * @return A Conversa atualizada.
     * @throws ResourceNotFoundException Se a conversa não for encontrada.
     */
    @Transactional
    public Conversa atualizarUltimaMensagemEm(Long conversaId, LocalDateTime dataHora) {
        Conversa conversa = buscarPorId(conversaId);
        conversa.setUltimaMensagemEm(dataHora);
        return conversaRepository.save(conversa);
    }

    // =========================================================================
    // D - DELETE (Deletar)
    // =========================================================================

    /**
     * Deleta uma Conversa pelo ID (Chave Primária).
     *
     * @param conversaId O ID da Conversa a ser deletada.
     * @throws ResourceNotFoundException Se a conversa não for encontrada.
     */
    @SuppressWarnings("null")
    @Transactional
    public void deletar(Long conversaId) {
        // Busca primeiro para garantir que a exceção de "não encontrado" seja lançada
        Conversa conversaExistente = buscarPorId(conversaId);
        conversaRepository.delete(conversaExistente);
    }
}