package br.com.wta.frete.core.service;

import br.com.wta.frete.core.controller.dto.PessoaRequest;
import br.com.wta.frete.core.controller.dto.CadastroSimplificadoRequest;
import br.com.wta.frete.core.entity.Perfil;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.PessoaPerfil;
import br.com.wta.frete.core.repository.PerfilRepository;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.core.service.mapper.PessoaMapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

/**
 * Service dedicado à lógica de CADASTRO da entidade Pessoa (completo e
 * simplificado).
 * Assume a responsabilidade de validação de unicidade, mapeamento,
 * criptografia e iniciação do processo de ativação.
 */
@Service
public class CadastroPessoaService {

    private final PessoaRepository pessoaRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final PessoaMapper pessoaMapper;
    private final TokenAtivacaoService tokenAtivacaoService;
    private final EmailService emailService;

    // Injeção de Dependências
    public CadastroPessoaService(
            PessoaRepository pessoaRepository,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder,
            PessoaMapper pessoaMapper,
            TokenAtivacaoService tokenAtivacaoService,
            EmailService emailService) {
        this.pessoaRepository = pessoaRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.pessoaMapper = pessoaMapper;
        this.tokenAtivacaoService = tokenAtivacaoService;
        this.emailService = emailService;
    }

    /**
     * Documentação: Cadastra uma nova Pessoa (completa), validando e-mail e
     * documento, e dispara o processo de ativação.
     * 
     * @param request DTO de requisição de cadastro completo.
     * @return A entidade Pessoa recém-cadastrada.
     */
    @Transactional
    public Pessoa cadastrarPessoa(PessoaRequest request) {
        // 1. VALIDAÇÃO DE UNICIDADE (E-mail e Documento)
        if (pessoaRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Erro de Cadastro: O e-mail já está em uso.");
        }
        if (pessoaRepository.findByDocumento(request.documento()).isPresent()) {
            throw new IllegalArgumentException("Erro de Cadastro: O documento (CPF/CNPJ) já está em uso.");
        }

        // 2. CONVERSÃO DTO -> ENTIDADE (Mapeamento completo)
        Pessoa novaPessoa = pessoaMapper.toEntity(request);

        // 3. DELEGAÇÃO DA LÓGICA COMPARTILHADA
        return processarCadastro(novaPessoa, request.senha());
    }

    /**
     * Documentação: Cadastra uma nova Pessoa (simplificada: e-mail/senha).
     * Usa placeholders para 'nome' e 'documento' e dispara o processo de ativação.
     * 
     * @param request DTO de requisição de cadastro simplificado.
     * @return A entidade Pessoa recém-cadastrada.
     */
    @Transactional
    public Pessoa cadastrarPessoaSimplificado(CadastroSimplificadoRequest request) {
        // 1. VALIDAÇÃO DE UNICIDADE (APENAS E-MAIL)
        if (pessoaRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Erro de Cadastro: O e-mail já está em uso.");
        }

        // 2. CONVERSÃO DTO -> ENTIDADE (Mapeamento simplificado com placeholders)
        Pessoa novaPessoa = pessoaMapper.toEntity(request);

        // 3. DELEGAÇÃO DA LÓGICA COMPARTILHADA
        return processarCadastro(novaPessoa, request.senha());
    }

    /**
     * Documentação: Método privado auxiliar que contém toda a lógica comum de
     * cadastro, aplicando o princípio DRY (Don't Repeat Yourself).
     * 
     * @param novaPessoa A Entidade Pessoa (já mapeada) a ser processada.
     * @param senhaPlana A senha em formato de texto para criptografia.
     * @return A entidade Pessoa salva e com o processo de ativação iniciado.
     */
    private Pessoa processarCadastro(Pessoa novaPessoa, String senhaPlana) {

        // 2.1. DEFINIÇÃO DA DATA DE CADASTRO
        novaPessoa.setDataCadastro(LocalDateTime.now(ZoneOffset.UTC));

        // 3. ENCODE DA SENHA (Criptografia)
        String senhaHash = passwordEncoder.encode(senhaPlana);
        novaPessoa.setSenha(senhaHash);

        // 4. ATRIBUIÇÃO DOS PERFIS PADRÕES ('LEAD' e 'CLIENTE')

        // 4.1. Busca o perfil LEAD (Perfil padrão inicial)
        Perfil perfilLead = perfilRepository.findByNomePerfil("LEAD")
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil 'LEAD' não encontrado no banco. Verifique a inicialização de dados."));

        // NOVO: 4.2. Busca o perfil CLIENTE
        Perfil perfilCliente = perfilRepository.findByNomePerfil("CLIENTE")
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil 'CLIENTE' não encontrado no banco. Verifique a inicialização de dados."));

        // 4.3. Criação e associação dos perfis
        Set<PessoaPerfil> perfis = new HashSet<>();

        // Adiciona LEAD
        perfis.add(new PessoaPerfil(novaPessoa, perfilLead));

        // Adiciona CLIENTE
        perfis.add(new PessoaPerfil(novaPessoa, perfilCliente));

        novaPessoa.setPerfis(perfis);
        novaPessoa.setCliente(true); // ALTERADO: Define o usuário como cliente em qualquer
                                     // cadastro.novaPessoa.setColaborador(false);
        novaPessoa.setColaborador(false);

        // 5. SALVAR NO BANCO DE DADOS (Pessoa ID é gerado aqui)
        Pessoa pessoaSalva = pessoaRepository.save(novaPessoa);

        // =================================================================
        // PROCESSO DE ATIVAÇÃO DE CONTA (Fases 1 e 2)
        // =================================================================

        // 6. GERAÇÃO E ARMAZENAMENTO DO TOKEN NO REDIS (Fase 1)
        String token = tokenAtivacaoService.criarToken(pessoaSalva.getId());

        // 7. ENVIO DO E-MAIL DE ATIVAÇÃO (Fase 2)
        emailService.enviarEmailAtivacao(pessoaSalva.getEmail(), token);

        return pessoaSalva;
    }
}