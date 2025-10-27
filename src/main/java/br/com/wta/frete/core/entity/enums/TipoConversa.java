package br.com.wta.frete.core.entity.enums;

/**
 * Define os tipos válidos para uma Conversa no sistema (Mapeado como STRING no
 * DB).
 */
public enum TipoConversa {

	PRIVADA, // Ex: Chat 1:1 entre Vendedor e Comprador
	GRUPO, // Ex: Chat com vários participantes
	SUPORTE; // Ex: Conversa com a equipe de Suporte

}