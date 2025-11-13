package br.com.wta.frete.core.controller;

import br.com.wta.frete.core.controller.dto.ContaDigitalResponse;
import br.com.wta.frete.core.controller.dto.StatusKycUpdateRequest;
import br.com.wta.frete.core.entity.ContaDigital;
import br.com.wta.frete.core.service.ContaDigitalService;
import br.com.wta.frete.core.service.mapper.ContaDigitalMapper;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para a gestão de Contas Digitais (Wallets).
 * Endpoints: /api/v1/contas-digitais
 */
@RestController // Define a classe como um Controller REST
@RequestMapping("/api/v1/contas-digitais") // URL base para todos os endpoints
public class ContaDigitalController {

    private final ContaDigitalService contaDigitalService;
    private final ContaDigitalMapper mapper;

    // Injeção de dependência via construtor
    public ContaDigitalController(ContaDigitalService contaDigitalService, ContaDigitalMapper mapper) {
        this.contaDigitalService = contaDigitalService;
        this.mapper = mapper;
    }

    // =========================================================================
    // C - CREATE (Criação/Abertura de Conta)
    // =========================================================================

    /**
     * POST /api/v1/contas-digitais/{pessoaId}
     * Abre uma nova conta digital para a pessoa especificada.
     *
     * @param pessoaId O ID da Pessoa para qual a conta será aberta.
     * @return ResponseEntity com o DTO da Conta recém-criada e status 201 CREATED.
     */
    @PostMapping("/{pessoaId}")
    public ResponseEntity<ContaDigitalResponse> abrirConta(@PathVariable Long pessoaId) {
        // Lógica: Service abre a conta, gera UUID e define Status KYC PENDENTE
        ContaDigital novaConta = contaDigitalService.abrirNovaConta(pessoaId);

        // Converte a entidade para DTO de resposta
        ContaDigitalResponse response = mapper.toResponse(novaConta);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // =========================================================================
    // R - READ (Leitura/Busca)
    // =========================================================================

    /**
     * GET /api/v1/contas-digitais/{pessoaId}
     * Busca uma conta digital pelo ID da Pessoa.
     *
     * @param pessoaId O ID da Pessoa/Conta.
     * @return ResponseEntity com o DTO da Conta e status 200 OK.
     * @throws ResourceNotFoundException Se a conta não for encontrada (tratada
     *                                   globalmente).
     */
    @GetMapping("/{pessoaId}")
    public ResponseEntity<ContaDigitalResponse> buscarContaPorPessoaId(@PathVariable Long pessoaId) {
        ContaDigital conta = contaDigitalService.buscarPorId(pessoaId);
        return ResponseEntity.ok(mapper.toResponse(conta));
    }

    /**
     * GET /api/v1/contas-digitais/uuid/{contaUuid}
     * Busca uma conta digital pelo seu UUID único.
     *
     * @param contaUuid O UUID da conta.
     * @return ResponseEntity com o DTO da Conta e status 200 OK, ou 404 se não
     *         encontrada.
     */
    @GetMapping("/uuid/{contaUuid}")
    public ResponseEntity<ContaDigitalResponse> buscarContaPorUuid(@PathVariable String contaUuid) {
        ContaDigital conta = contaDigitalService.buscarPorContaUuid(contaUuid);

        // Se o findByContaUuid retornar null (não lança exceção como o findById)
        if (conta == null) {
            // CORREÇÃO: Construtor da ResourceNotFoundException ajustado para 2 argumentos
            throw new ResourceNotFoundException(
                    String.format("ContaDigital não encontrada para o contaUuid: '%s'", contaUuid),
                    "CONTA_DIGITAL_NAO_ENCONTRADA");
        }

        return ResponseEntity.ok(mapper.toResponse(conta));
    }

    /**
     * GET /api/v1/contas-digitais
     * Lista todas as contas digitais.
     *
     * @return Lista de DTOs ContaDigitalResponse.
     */
    @GetMapping
    public ResponseEntity<List<ContaDigitalResponse>> listarTodasContas() {
        List<ContaDigital> contas = contaDigitalService.listarTodas();
        // MÉTODO toResponseList AGORA DISPONÍVEL NO MAPPER
        return ResponseEntity.ok(mapper.toResponseList(contas));
    }

    // =========================================================================
    // U - UPDATE (Atualização de Status KYC)
    // =========================================================================

    /**
     * PUT /api/v1/contas-digitais/{pessoaId}/kyc
     * Atualiza o status KYC da conta digital da pessoa.
     *
     * @param pessoaId O ID da Pessoa/Conta a ser atualizada.
     * @param request  O DTO com o novo StatusKYC.
     * @return ResponseEntity com o DTO da Conta atualizada e status 200 OK.
     */
    @PutMapping("/{pessoaId}/kyc")
    public ResponseEntity<ContaDigitalResponse> atualizarKyc(
            @PathVariable Long pessoaId,
            @Valid @RequestBody StatusKycUpdateRequest request) { // @Valid para validação básica do DTO

        ContaDigital contaAtualizada = contaDigitalService.atualizarStatusKyc(pessoaId, request.getStatusKyc());
        return ResponseEntity.ok(mapper.toResponse(contaAtualizada));
    }

    // =========================================================================
    // D - DELETE (Deletar Conta)
    // =========================================================================

    /**
     * DELETE /api/v1/contas-digitais/{pessoaId}
     * Deleta a conta digital associada à Pessoa.
     *
     * @param pessoaId O ID da Pessoa/Conta a ser deletada.
     * @return ResponseEntity sem conteúdo (No Content) e status 204 NO CONTENT.
     */
    @DeleteMapping("/{pessoaId}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long pessoaId) {
        contaDigitalService.deletar(pessoaId);
        // O status 204 é o padrão para deleção bem-sucedida, sem corpo de resposta
        return ResponseEntity.noContent().build();
    }
}