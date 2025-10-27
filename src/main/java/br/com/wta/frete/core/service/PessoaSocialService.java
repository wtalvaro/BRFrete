package br.com.wta.frete.core.service;

import br.com.wta.frete.core.controller.dto.OAuth2UserRequestDTO;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.repository.PessoaRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Service dedicado à lógica de CADASTRO e LOGIN Social
 * (OAuth2).
 */
@Service
public class PessoaSocialService {

    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    // Injeção de Dependências
    public PessoaSocialService(
            PessoaRepository pessoaRepository,
            PasswordEncoder passwordEncoder) {
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * MÉTODO DE NEGÓCIO: cadastrarOuObterPessoaSocial
     * Propósito: Lida com a lógica de Login e Cadastro para utilizadores OAuth2.
     * Regras de Coexistência:
     * 1. Se o socialId JÁ EXISTE: Retorna a entidade Pessoa existente (LOGIN).
     * 2. Se o e-mail JÁ EXISTE (mas sem socialId): Liga a conta existente.
     * 3. Se NÃO EXISTE: Cria uma nova Pessoa (CADASTRO SOCIAL) e ativa
     * imediatamente.
     *
     * @param request DTO com dados do utilizador OAuth2.
     * @return A entidade Pessoa existente ou recém-criada.
     */
    @Transactional
    public Pessoa cadastrarOuObterPessoaSocial(OAuth2UserRequestDTO request) {

        // 1. TENTA ENCONTRAR PELO socialId
        return pessoaRepository.findBySocialId(request.getSocialId()) // ⚠️ Assumindo findBySocialId
                .orElseGet(() -> {

                    // 2. TENTA ENCONTRAR PELO E-MAIL (Ligação com conta existente)
                    Pessoa pessoaExistente = pessoaRepository.findByEmail(request.getEmail()).orElse(null);

                    if (pessoaExistente != null) {
                        // 2.1. LIGAÇÃO: Conta existente (pelo email) com novo login social
                        pessoaExistente.setSocialId(request.getSocialId());
                        // Ativa a conta se ainda não estiver ativa (se veio de um cadastro normal
                        // pendente)
                        if (!pessoaExistente.isAtivo()) {
                            pessoaExistente.setAtivo(true);
                        }
                        System.out.println("Conta existente ligada ao login social: " + request.getEmail());
                        return pessoaRepository.save(pessoaExistente);
                    }

                    // 3. SE NÃO EXISTIR, INICIA CADASTRO SOCIAL

                    Pessoa novaPessoa = new Pessoa();
                    novaPessoa.setNome(request.getNome());
                    novaPessoa.setEmail(request.getEmail());
                    novaPessoa.setSocialId(request.getSocialId());

                    // A senha é NOT NULL, então preenchemos com um placeholder encriptado.
                    novaPessoa.setSenha(passwordEncoder.encode("SOCIAL_AUTH_" + request.getProvedor()));

                    // --- ATIVAÇÃO IMEDIATA ---
                    novaPessoa.setAtivo(true);
                    novaPessoa.setDataCadastro(LocalDateTime.now(ZoneOffset.UTC));
                    novaPessoa.setCliente(false);
                    novaPessoa.setColaborador(false);
                    // O campo 'documento' pode ser NULL ou placeholder UUID

                    // 4. SALVA NO BANCO
                    System.out.println("Novo utilizador social cadastrado: " + request.getEmail() + " via "
                            + request.getProvedor());
                    return pessoaRepository.save(novaPessoa);
                });
    }
}