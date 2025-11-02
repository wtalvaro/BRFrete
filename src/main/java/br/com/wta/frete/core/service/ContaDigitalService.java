package br.com.wta.frete.core.service;

import br.com.wta.frete.core.entity.ContaDigital;
import br.com.wta.frete.core.entity.enums.StatusKYC; // Assumindo que você criou o ENUM
import br.com.wta.frete.core.repository.ContaDigitalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class ContaDigitalService {

    private final ContaDigitalRepository contaDigitalRepository;

    public ContaDigitalService(ContaDigitalRepository contaDigitalRepository) {
        this.contaDigitalRepository = contaDigitalRepository;
    }

    /**
     * Abre a conta digital (Wallet) para a Pessoa recém-cadastrada. Necessário para
     * todos os colaboradores que recebem pagamentos.
     * 
     * @param pessoaId O ID da pessoa (chave primária/estrangeira).
     * @return A entidade ContaDigital criada.
     */
    @Transactional
    public ContaDigital abrirNovaConta(Long pessoaId) {

        // 1. Geração de um ID de Conta Único (UUID)
        // Isso simula o ID fornecido por um provedor FinTech externo
        String contaUuid = UUID.randomUUID().toString(); // Mapeia para conta_uuid

        // 2. Criação da Entidade
        ContaDigital conta = new ContaDigital();

        // A chave primária é a FK para core.pessoas
        conta.setPessoaId(pessoaId);

        // O UUID da conta no sistema financeiro
        conta.setContaUuid(contaUuid);

        // Status inicial de verificação de identidade (Know Your Customer)
        // O status é 'PENDENTE' por default, aguardando documentos
        conta.setStatusKyc(StatusKYC.PENDENTE);

        // A data_abertura é definida por DEFAULT no SQL ou no
        // @CreationTimestamp

        // 3. Persistência
        return contaDigitalRepository.save(conta);
    }
}