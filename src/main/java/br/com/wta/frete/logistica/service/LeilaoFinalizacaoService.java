package br.com.wta.frete.logistica.service;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.logistica.entity.Frete;
import br.com.wta.frete.logistica.entity.Lance;
import br.com.wta.frete.logistica.repository.FreteRepository;
import br.com.wta.frete.logistica.repository.LanceRepository;
import br.com.wta.frete.logistica.repository.StatusLeilaoRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Componente dedicado para finalizar um leilão de frete e orquestrar a
 * confirmação da Ordem de Serviço (OS), quebrando a dependência circular
 * entre FreteService e OrdemServicoService.
 */
@Service
@RequiredArgsConstructor
public class LeilaoFinalizacaoService {

    private static final Logger log = LoggerFactory.getLogger(LeilaoFinalizacaoService.class);

    // VAI DEIXAR DE SER "final" se usarmos @Autowired no campo sem
    // @RequiredArgsConstructor.
    // O mais limpo, mantendo o @RequiredArgsConstructor:
    // A anotação @Lazy é aplicada ao parâmetro do construtor,
    // que é gerado pelo Lombok. Como não podemos modificar o construtor,
    // faremos a injeção diretamente no campo (field injection) e marcando o campo
    // como @Lazy,
    // mas isso requer o @Autowired ou usar o @RequiredArgsConstructor e adicionar o
    // @Lazy no construtor.
    // Vamos usar a injeção de campo para simplificar o Lombok.

    // CORREÇÃO: Usando injeção de campo com @Autowired e @Lazy.
    // Isso garante que o bean OrdemServicoService não precisa estar totalmente
    // pronto
    // no momento da criação do LeilaoFinalizacaoService.
    @Lazy
    private final OrdemServicoService ordemServicoService; // <<< AQUI ESTÁ O FIX REAL

    private final LanceRepository lanceRepository;
    @SuppressWarnings("unused")
    private final FreteRepository freteRepository;
    @SuppressWarnings("unused")
    private final StatusLeilaoRepository statusLeilaoRepository;

    /**
     * Finaliza o leilão com um lance vencedor e confirma a Ordem de Serviço (OS).
     * 
     * @param frete         A entidade Frete (já atualizada com status e valor
     *                      aceito).
     * @param lanceVencedor O Lance vencedor.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finalizarLeilaoEConfirmarOS(Frete frete, Lance lanceVencedor) {

        try {
            // 1. Atualiza o Lance como vencedor
            lanceVencedor.setVencedor(true);
            lanceRepository.save(lanceVencedor);

            // 2. Confirma a Ordem de Serviço (OS)
            ordemServicoService.confirmarOsComLanceVencedor(
                    frete.getOrdemServico().getId(), // ID da OS
                    lanceVencedor);

            log.info("OS #{} confirmada com sucesso pelo lance vencedor (R$ {}).",
                    frete.getOrdemServico().getId(),
                    lanceVencedor.getValorLance());

        } catch (ResourceNotFoundException e) {
            log.error("Erro ao confirmar OS #{} após leilão. Detalhe: {}",
                    frete.getOrdemServico().getId(), e.getMessage());
            // A transação separada (@Transactional(propagation = Propagation.REQUIRES_NEW))
            // garante que a falha aqui não reverta o ENCERRADO_COM_VENCEDOR no
            // FreteService.
        }
    }
}