package br.com.wta.frete.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.core.entity.Conversa;
import br.com.wta.frete.core.entity.Mensagem;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.MensagemRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;

/**
 * Camada de Serviço para a entidade Mensagem (core.mensagens).
 * Contém a lógica de negócio para envio, leitura e gestão de mensagens em
 * chats.
 */
@Service
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final ConversaService conversaService; // Depende do serviço de Conversa
    private final PessoaService pessoaService; // Depende do serviço de Pessoa (assumindo que existe)

    public MensagemService(MensagemRepository mensagemRepository,
            ConversaService conversaService,
            PessoaService pessoaService) {
        this.mensagemRepository = mensagemRepository;
        this.conversaService = conversaService;
        this.pessoaService = pessoaService;
    }

    // =========================================================================
    // C - CREATE (Enviar Mensagem)
    // =========================================================================

    /**
     * Envia e persiste uma nova mensagem no banco.
     * <p>
     * Nota: O Mapper MensagemMapper mapeou apenas os campos simples. Esta camada
     * é responsável por buscar as entidades de relacionamento (Conversa e Autor).
     *
     * @param mensagem   A entidade Mensagem, com IDs de Conversa e Autor já
     *                   preenchidos pelo Mapper.
     * @param conversaId O ID da conversa onde a mensagem será enviada.
     * @param autorId    O ID da Pessoa que está enviando a mensagem.
     * @return A Mensagem salva.
     */
    @Transactional
    public Mensagem enviarMensagem(Mensagem mensagem, Long conversaId, Long autorId) {

        // 1. Validar e buscar entidades de relacionamento
        Conversa conversa = conversaService.buscarPorId(conversaId); // Lançará 404 se não existir
        Pessoa autor = pessoaService.buscarPorId(autorId); // Lançará 404 se não existir (Assumindo método)

        // 2. Definir relacionamentos e metadados
        mensagem.setConversa(conversa);
        mensagem.setAutor(autor);
        // dataEnvio e isLida são setados como padrão na entidade

        // 3. Persistência
        Mensagem mensagemSalva = mensagemRepository.save(mensagem);

        // 4. Lógica de Negócio Adicional: Atualizar a Conversa
        // Atualiza a data da última mensagem na conversa pai
        conversaService.atualizarUltimaMensagemEm(conversaId, mensagemSalva.getDataEnvio());

        return mensagemSalva;
    }

    // =========================================================================
    // R - READ (Ler/Buscar)
    // =========================================================================

    /**
     * Busca uma Mensagem pelo ID (Chave Primária).
     *
     * @param mensagemId O ID da Mensagem.
     * @return A Mensagem encontrada.
     * @throws ResourceNotFoundException Se a mensagem não for encontrada.
     */
    @SuppressWarnings("null")
    public Mensagem buscarPorId(Long mensagemId) {
        return mensagemRepository.findById(mensagemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Mensagem não encontrada para o ID: '%d'", mensagemId),
                        "MENSAGEM_NAO_ENCONTRADA_ID"));
    }

    /**
     * Lista todas as mensagens de uma conversa, ordenadas por data de envio.
     *
     * @param conversaId O ID da Conversa.
     * @return Uma lista de objetos Mensagem.
     */
    public List<Mensagem> listarPorConversaId(Long conversaId) {
        // CORREÇÃO: Utiliza o novo método otimizado do Repositório.
        return mensagemRepository.findByConversaIdOrderByDataEnvioAsc(conversaId);
    }

    // ... (Outros métodos como listarTodas se necessário)

    // =========================================================================
    // U - UPDATE (Marcar como Lida)
    // =========================================================================

    /**
     * Marca uma mensagem específica como lida.
     *
     * @param mensagemId O ID da Mensagem a ser marcada.
     * @return A Mensagem atualizada.
     * @throws ResourceNotFoundException Se a mensagem não for encontrada.
     */
    @Transactional
    public Mensagem marcarComoLida(Long mensagemId) {
        Mensagem mensagem = buscarPorId(mensagemId);
        if (!mensagem.getIsLida()) {
            mensagem.setIsLida(true);
            return mensagemRepository.save(mensagem);
        }
        return mensagem;
    }

    // =========================================================================
    // D - DELETE (Deletar)
    // =========================================================================

    /**
     * Deleta uma Mensagem pelo ID.
     *
     * @param mensagemId O ID da Mensagem a ser deletada.
     * @throws ResourceNotFoundException Se a mensagem não for encontrada.
     */
    @SuppressWarnings("null")
    @Transactional
    public void deletar(Long mensagemId) {
        Mensagem mensagemExistente = buscarPorId(mensagemId);
        mensagemRepository.delete(mensagemExistente);
    }
}