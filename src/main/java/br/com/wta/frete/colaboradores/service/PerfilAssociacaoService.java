package br.com.wta.frete.colaboradores.service;

import br.com.wta.frete.core.entity.Perfil;
import br.com.wta.frete.core.entity.Pessoa;
import br.com.wta.frete.core.entity.PessoaPerfil;
import br.com.wta.frete.core.entity.PessoaPerfilId;
import br.com.wta.frete.core.repository.PerfilRepository;
import br.com.wta.frete.core.repository.PessoaPerfilRepository;
import br.com.wta.frete.core.repository.PessoaRepository;
import br.com.wta.frete.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service utilitário para centralizar a lógica de gerenciamento de perfis de
 * Colaboradores.
 * Reduz a repetição de código nos serviços específicos de Catador, Lojista,
 * etc.
 */
@Service
public class PerfilAssociacaoService {

    private final PessoaRepository pessoaRepository;
    private final PerfilRepository perfilRepository;
    private final PessoaPerfilRepository pessoaPerfilRepository;

    public PerfilAssociacaoService(PessoaRepository pessoaRepository,
            PerfilRepository perfilRepository,
            PessoaPerfilRepository pessoaPerfilRepository) {
        this.pessoaRepository = pessoaRepository;
        this.perfilRepository = perfilRepository;
        this.pessoaPerfilRepository = pessoaPerfilRepository;
    }

    /**
     * Documentação: Método para validar a Pessoa, buscar o Perfil, associar o
     * Perfil
     * à Pessoa (se já não estiver associado) e atualizar o flag isColaborador.
     *
     * Este método executa a lógica repetitiva que estava em todos os services de
     * colaborador.
     *
     * @param pessoaId   O ID da Pessoa a ser verificada/atualizada.
     * @param nomePerfil O nome do Perfil a ser associado (ex: "CATADOR").
     * @return A entidade Pessoa, já gerenciada e com o flag isColaborador
     *         atualizado.
     */
    @SuppressWarnings("null")
    @Transactional
    public Pessoa associarPerfilColaborador(Long pessoaId, String nomePerfil) {

        // 1. Busca a Pessoa (e garante que ela existe)
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + pessoaId));

        // 2. Busca o Perfil (e garante que ele existe)
        Perfil perfil = perfilRepository.findByNomePerfil(nomePerfil)
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil '" + nomePerfil + "' não encontrado. Verifique a inicialização de dados."));

        // 3. Adiciona/Atualiza o Perfil de Colaborador à Pessoa (Lógica de M:M)
        PessoaPerfilId pessoaPerfilId = new PessoaPerfilId(pessoaId, perfil.getId());

        // Verifica se a associação já existe antes de tentar criá-la (Idempotência)
        if (pessoaPerfilRepository.findById(pessoaPerfilId).isEmpty()) {
            PessoaPerfil novaPessoaPerfil = new PessoaPerfil(pessoa, perfil);
            pessoaPerfilRepository.save(novaPessoaPerfil);
        }

        // 4. Atualiza o flag isColaborador na Pessoa
        if (!pessoa.isColaborador()) {
            pessoa.setColaborador(true);
            // Como estamos em um @Transactional, o save pode ser omitido se a entidade
            // estiver gerenciada, mas para clareza e consistência, vamos salvá-la
            // (e é o que era feito nos seus serviços originais).
            pessoaRepository.save(pessoa);
        }

        return pessoa;
    }
}