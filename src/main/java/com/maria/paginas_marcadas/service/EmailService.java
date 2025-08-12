package com.maria.paginas_marcadas.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendValidationEmail(String destinatario, String nomeUsuario, String token) {
        String assunto = "Validação de E-mail - Páginas Marcadas";

        String conteudo = """
                <html>
                <body style="font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; text-align: center;">
                        <h2 style="color: #4a148c;">Confirme seu endereço de e-mail</h2>
                        <p>Olá, %s!</p>
                        <p>Estamos quase lá! Para começar a explorar e organizar suas leituras no Páginas Marcadas, insira o código abaixo na página de confirmação:</p>
                        <div style="margin-top: 20px; padding: 12px; background-color: #f3e5f5; border-radius: 8px; font-size: 1.5em; font-weight: bold; color: #4a148c; display: inline-block;">
                            %s
                        </div>
                        <p style="margin-top: 20px; font-size: 0.9em; color: #555;">Se não esperava por este e-mail, ignore-o.</p>
                    </div>
                </body>
                </html>
                """.formatted(nomeUsuario, token);

        enviarEmail(destinatario, assunto, conteudo);
    }

    public void sendWelcomeEmail(String destinatario, String nomeUsuario) {
        String assunto = "Bem-vindo ao Páginas Marcadas!";

        String conteudo = """
                <html>
                <body style="font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; text-align: center;">
                        <h2 style="color: #4a148c;">Bem-vindo(a) ao Páginas Marcadas!</h2>
                        <p>Olá, %s!</p>
                        <p>Estamos felizes em ter você com a gente! Explore e organize suas leituras de forma prática e divertida.</p>
                        <p style="margin-top: 20px; font-size: 0.9em; color: #555;">Esperamos que nossa plataforma estimule a felicidade de ler um bom livro.</p>
                    </div>
                </body>
                </html>
                """.formatted(nomeUsuario);

        enviarEmail(destinatario, assunto, conteudo);
    }

    public void sendRecoveryEmail(String destinatario, String nomeUsuario, String token) {
        String assunto = "Recupere sua senha - Páginas Marcadas";

        String conteudo = """
                <html>
                <body style="font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; text-align: center;">
                        <h2 style="color: #4a148c;">Redefinição de senha</h2>
                        <p>Olá, %s!</p>
                        <p>Recebemos uma solicitação para redefinir sua senha. Use o código abaixo para continuar o processo:</p>
                        <div style="margin-top: 20px; padding: 12px; background-color: #f3e5f5; border-radius: 8px; font-size: 1.5em; font-weight: bold; color: #4a148c; display: inline-block;">
                            %s
                        </div>
                        <p style="margin-top: 20px; font-size: 0.9em; color: #555;">Se você não solicitou a alteração de senha, ignore este e-mail.</p>
                    </div>
                </body>
                </html>
                """.formatted(nomeUsuario, token);

        enviarEmail(destinatario, assunto, conteudo);
    }

    private void enviarEmail(String destinatario, String assunto, String conteudo) {
        try {
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);

            javaMailSender.send(mensagem);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage(), e);
        }
    }
}
