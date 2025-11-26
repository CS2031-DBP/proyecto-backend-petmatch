package org.example.petmatch.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.name}")
    private String appName;

    @Value("${app.email}")
    private String appEmail;

    /**
     * Envío de email simple (texto plano)
     */
    @Async
    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(appEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
        }
    }

    /**
     * Envío de email con HTML
     */
    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(appEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = es HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Error al enviar email HTML: " + e.getMessage());
        }
    }

    /**
     * Email de bienvenida para Usuario
     */
    @Async
    public void sendWelcomeEmailUser(String to, String name) {
        String subject = "¡Bienvenido a " + appName + "!";
        String body = buildWelcomeEmailUser(name);
        sendHtmlEmail(to, subject, body);
    }

    /**
     * Email de bienvenida para Albergue
     */
    @Async
    public void sendWelcomeEmailAlbergue(String to, String nombreAlbergue) {
        String subject = "¡Bienvenido a " + appName + "!";
        String body = buildWelcomeEmailAlbergue(nombreAlbergue);
        sendHtmlEmail(to, subject, body);
    }

    // Plantilla HTML para Usuario
    private String buildWelcomeEmailUser(String name) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                    .container { background-color: white; padding: 30px; border-radius: 10px; max-width: 600px; margin: 0 auto; }
                    h1 { color: #4CAF50; }
                    .footer { margin-top: 30px; font-size: 12px; color: #999; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🐾 ¡Bienvenido a %s!</h1>
                    <p>Hola <strong>%s</strong>,</p>
                    <p>¡Gracias por registrarte en nuestra plataforma! Estamos emocionados de tenerte con nosotros.</p>
                    <p>Ahora puedes:</p>
                    <ul>
                        <li>Buscar albergues cerca de ti</li>
                        <li>Adoptar mascotas</li>
                        <li>Ser voluntario en programas</li>
                        <li>Donar a los albergues</li>
                    </ul>
                    <p>¡Comienza a explorar y ayuda a darles un hogar a nuestros amigos peludos! 🐶🐱</p>
                    <div class="footer">
                        <p>Este es un correo automático, por favor no respondas.</p>
                        <p>&copy; 2025 %s. Todos los derechos reservados.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(appName, name, appName);
    }

    // Plantilla HTML para Albergue
    private String buildWelcomeEmailAlbergue(String nombreAlbergue) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                    .container { background-color: white; padding: 30px; border-radius: 10px; max-width: 600px; margin: 0 auto; }
                    h1 { color: #FF9800; }
                    .footer { margin-top: 30px; font-size: 12px; color: #999; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🏠 ¡Bienvenido a %s!</h1>
                    <p>Hola <strong>%s</strong>,</p>
                    <p>¡Gracias por registrar tu albergue en nuestra plataforma!</p>
                    <p>Ahora puedes:</p>
                    <ul>
                        <li>Publicar mascotas en adopción</li>
                        <li>Crear programas de voluntariado</li>
                        <li>Recibir donaciones</li>
                        <li>Gestionar tus espacios disponibles</li>
                    </ul>
                    <p>¡Gracias por tu labor en rescatar y cuidar a nuestros amigos! 🐾</p>
                    <div class="footer">
                        <p>Este es un correo automático, por favor no respondas.</p>
                        <p>&copy; 2025 %s. Todos los derechos reservados.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(appName, nombreAlbergue, appName);
    }
}
