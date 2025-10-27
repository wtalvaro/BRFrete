package br.com.wta.frete.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Serviço: EmailService
 * Descrição: Responsável por enviar e-mails transacionais (ativação,
 * recuperação).
 */
@Service
public class EmailService {

    // Documentação: Objeto principal do Spring para enviar e-mails.
    private final JavaMailSender mailSender;

    // Documentação: Injeta o valor da propriedade
    // 'spring.mail.properties.mail.smtp.from'
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String remetente;

    // =================================================================
    // CORREÇÃO: Remova a linha 'private static final String BASE_URL_ATIVACAO =
    // ...'
    // E ADICIONE ESTAS DUAS NOVAS VARIÁVEIS:

    // 1. Injeta o valor da URL base (app.base.url)
    @Value("${app.base.url}")
    private String baseUrl;

    // 2. O caminho FIXO da API
    private static final String PATH_ATIVACAO = "/api/v1/ativacao?token=";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Método Educativo: enviarEmailAtivacao
     * Constrói e envia o e-mail com o link de ativação, incluindo o token.
     * 
     * @param destinatarioEmail O e-mail para o qual a mensagem será enviada.
     * @param token             O token de ativação gerado pelo Redis.
     */
    public void enviarEmailAtivacao(String destinatarioEmail, String token) {

        // 1. Cria a mensagem de e-mail
        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom(remetente);
        mensagem.setTo(destinatarioEmail);
        mensagem.setSubject("BRFrete: Ativação de Cadastro");

        // 2. Constrói o corpo do e-mail com o link de ativação
        String linkAtivacao = baseUrl + PATH_ATIVACAO + token;

        String corpoEmail = "Olá! \n\n" +
                "Obrigado por se cadastrar na BRFrete. Para ativar sua conta e começar a usar nossos serviços, clique no link abaixo:\n\n"
                +
                linkAtivacao + "\n\n" +
                "Este link é válido por 24 horas. Se você não solicitou este cadastro, por favor, ignore este e-mail.\n\n"
                +
                "Atenciosamente,\n" +
                "Equipe BRFrete";

        mensagem.setText(corpoEmail);

        // 3. Envia o e-mail (em ambiente de produção, é ideal fazer isso de forma
        // assíncrona)
        try {
            mailSender.send(mensagem);
            System.out.println("E-mail de ativação enviado com sucesso para: " + destinatarioEmail);
        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail de ativação para " + destinatarioEmail + ": " + e.getMessage());
            // Em produção, você registraria essa falha no log e talvez numa tabela de
            // erros.
            throw new RuntimeException("Não foi possível enviar o e-mail de ativação.", e);
        }
    }
}