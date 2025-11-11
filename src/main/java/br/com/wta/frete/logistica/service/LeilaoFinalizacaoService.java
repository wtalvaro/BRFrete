// Caminho: src/main/java/br/com/wta/frete/logistica/service/LeilaoFinalizacaoService.java
package br.com.wta.frete.logistica.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy; // Importação correta do Spring
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.wta.frete.logistica.entity.Frete;
import br.com.wta.frete.logistica.entity.Lance;
import br.com.wta.frete.logistica.repository.LanceRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Componente dedicado para finalizar um leilão de frete e orquestrar a
 * confirmação da Ordem de Serviço (OS), quebrando a dependência circular
 * entre FreteService e OrdemServicoService.
 *
 * Utiliza @Transactional(propagation = Propagation.REQUIRES_NEW) para garantir
 * que a confirmação da OS seja atômica e independente da transação de
 * encerramento
 * do Frete.
 */
@Service
@RequiredArgsConstructor
public class LeilaoFinalizacaoService {

    private static final Logger log = LoggerFactory.getLogger(LeilaoFinalizacaoService.class);

    // Injeção com @Lazy para quebrar a dependência circular e garantir o uso
    // correto da transação
    @Lazy
    private final OrdemServicoService ordemServicoService;

    private final LanceRepository lanceRepository;

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
                    lanceVencedor); // O Service usa o Lance para buscar o Transportador e o Valor

            log.info("OS #{} confirmada com sucesso pelo lance vencedor (R$ {}).",
                    frete.getOrdemServico().getId(),
                    lanceVencedor.getValorLance());

        } catch (ResourceNotFoundException e) {
            log.error("Erro ao confirmar OS #{} após leilão. Detalhe: {}",
                    frete.getOrdemServico().getId(), e.getMessage());
            // A falha aqui não reverá o status ENCERRADO_COM_VENCEDOR no Frete.
            throw new RuntimeException("Falha ao confirmar OS #" + frete.getOrdemServico().getId(), e);
        }
    }
}