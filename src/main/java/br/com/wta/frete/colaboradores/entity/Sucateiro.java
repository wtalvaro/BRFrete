package br.com.wta.frete.colaboradores.entity;

import br.com.wta.frete.core.entity.Pessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapeia a tabela 'colaboradores.sucateiros'. Especialização de Pessoa para
 * sucateiros (empresas de reciclagem/pátios). Relacionamento 1:1 com Pessoa via
 * chave compartilhada (@MapsId).
 * * =========================================================================
 * RESUMO ARQUITETURAL: Chave Derivada (@MapsId) em Sucateiro.
 * * * IDEIA DE DESIGN (Vantagens):
 * O uso de @MapsId (Chave Derivada) é a **solução arquitetônica mais correta e
 * elegante** * para modelar o 'Sucateiro' como uma **especialização
 * obrigatória** de 'Pessoa'.
 * Garante, ao nível do banco de dados, que o ID do Sucateiro é sempre o mesmo
 * ID da Pessoa
 * (Relação 1:1 Semântica Obrigatória), resultando em um esquema limpo e sem
 * chaves redundantes.
 * * * CUSTO DE IMPLEMENTAÇÃO (Desvantagens e Correção):
 * - Problema: Em entidades com Chave Derivada, o JPA/Hibernate tem dificuldade
 * em distinguir uma nova entidade de uma entidade 'detached' quando o ID é
 * preenchido. Isso
 * faz com que o `JpaRepository.save()` (que age como `merge()`) tente um
 * `UPDATE` em
 * vez de um `INSERT`, gerando o erro `StaleObjectStateException`.
 * - Correção: Para garantir o `INSERT` (criação), é obrigatório **definir
 * explicitamente** * o ID derivado
 * (`novoSucateiro.setPessoaId(pessoaId)`) no objeto e, em casos
 * extremos, usar `EntityManager.persist()` ou garantir que o `save()` funcione
 * corretamente com o ID preenchido.
 * * * CONCLUSÃO:
 * Manter o mapeamento @MapsId, pois ele é semanticamente correto. A lógica
 * adicional no Service é uma medida de contorno necessária para a
 * especificidade de implementação do JPA/Hibernate.
 * =========================================================================
 */
@Entity
@Table(name = "sucateiros", schema = "colaboradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucateiro {

	/**
	 * Chave primária (pessoa_id BIGINT). Herda a chave primária da entidade Pessoa.
	 */
	@Id
	@Column(name = "pessoa_id")
	private Long pessoaId;

	/**
	 * Relacionamento Um-para-Um com Pessoa.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "pessoa_id", nullable = false)
	private Pessoa pessoa;

	/**
	 * Razão social (VARCHAR(255) NOT NULL).
	 */
	@Column(name = "razao_social", nullable = false, length = 255)
	private String razaoSocial;

	/**
	 * CNPJ Secundário ou Inscrição Estadual (VARCHAR(18)).
	 */
	@Column(name = "cnpj_secundario", length = 18)
	private String cnpjSecundario;

	/**
	 * Número da licença ambiental (VARCHAR(100)).
	 */
	@Column(name = "licenca_ambiental", length = 100)
	private String licencaAmbiental;

	/**
	 * Endereço físico do pátio de sucata (TEXT NOT NULL).
	 */
	@Column(name = "endereco_patio", nullable = false, columnDefinition = "TEXT")
	private String enderecoPatio;
}