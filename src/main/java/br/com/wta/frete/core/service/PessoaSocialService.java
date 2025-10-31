package br.com.wta.frete.core.service;

import br.com.wta.frete.core.controller.dto.OAuth2UserRequestDTO;
import br.com.wta.frete.core.entity.Perfil; // <<--- IMPORTAR
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.PessoaPerfil; // <<--- IMPORTAR
import br.com.wta.frete.core.repository.PerfilRepository; // <<--- IMPORTAR
import br.com.wta.frete.core.repository.PessoaRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet; // <<--- IMPORTAR
import java.util.Set; // <<--- IMPORTAR

/**
 * Service dedicado à lógica de CADASTRO e LOGIN Social (OAuth2).
 */
@Service
public class PessoaSocialService {

    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;
    private final PerfilRepository perfilRepository; // <<--- INJETAR REPOSITÓRIO DE PERFIL

    // Injeção de Dependências
    public PessoaSocialService(
            PessoaRepository pessoaRepository,
            PasswordEncoder passwordEncoder,
            PerfilRepository perfilRepository // <<--- ADICIONAR AO CONSTRUTOR
    ) {
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
        this.perfilRepository = perfilRepository; // <<--- ATRIBUIR
    }

    /**
     * MÉTODO DE NEGÓCIO: cadastrarOuObterPessoaSocial
     * Propósito: Lida com a lógica de Login e Cadastro para utilizadores OAuth2.
     * ... (regras de coexistência) ...
     */
    @Transactional
    public Pessoa cadastrarOuObterPessoaSocial(OAuth2UserRequestDTO request) {

        // 1. TENTA ENCONTRAR PELO socialId (COM PERFIS)
        // <<--- CORREÇÃO 1: Usar o método com JOIN FETCH
        return pessoaRepository.findBySocialIdWithPerfis(request.getSocialId())
                .orElseGet(() -> {

                    // 2. TENTA ENCONTRAR PELO E-MAIL (COM PERFIS)
                    // <<--- CORREÇÃO 2: Usar o método com JOIN FETCH
                    Pessoa pessoaExistente = pessoaRepository.findByEmailWithPerfis(request.getEmail()).orElse(null);

                    if (pessoaExistente != null) {
                        // 2.1. LIGAÇÃO: Conta existente com novo login social
                        pessoaExistente.setSocialId(request.getSocialId());
                        if (!pessoaExistente.isAtivo()) {
                            pessoaExistente.setAtivo(true);
                        }
                        System.out.println("Conta existente ligada ao login social: " + request.getEmail());
                        return pessoaRepository.save(pessoaExistente); // Retorna a pessoa com perfis
                    }

                    // 3. SE NÃO EXISTIR, INICIA CADASTRO SOCIAL
                    System.out.println("Novo utilizador social cadastrado: " + request.getEmail() + " via "
                            + request.getProvedor());

                    Pessoa novaPessoa = new Pessoa();
                    novaPessoa.setNome(request.getNome());
                    novaPessoa.setEmail(request.getEmail());
                    novaPessoa.setSocialId(request.getSocialId());
                    novaPessoa.setSenha(passwordEncoder.encode("SOCIAL_AUTH_" + request.getProvedor()));
                    novaPessoa.setAtivo(true);
                    novaPessoa.setDataCadastro(LocalDateTime.now(ZoneOffset.UTC));
                    novaPessoa.setCliente(false);
                    novaPessoa.setColaborador(false);

                    // <<--- CORREÇÃO 3: ATRIBUIÇÃO DO PERFIL PADRÃO "LEAD" (BUG FIX)
                    Perfil perfilPadrao = perfilRepository.findByNomePerfil("LEAD");
                    if (perfilPadrao == null) {
                        throw new IllegalStateException(
                                "Perfil 'LEAD' não encontrado. Verifique a inicialização de dados.");
                    }
                    PessoaPerfil pessoaPerfil = new PessoaPerfil(novaPessoa, perfilPadrao);
                    Set<PessoaPerfil> perfis = new HashSet<>();
                    perfis.add(pessoaPerfil);
                    novaPessoa.setPerfis(perfis);
                    // --- FIM DA CORREÇÃO 3 ---

                    // 4. SALVA NO BANCO
                    // O objeto 'novaPessoa' agora contém os perfis ANTES de ser salvo.
                    return pessoaRepository.save(novaPessoa);
                });
    }
}