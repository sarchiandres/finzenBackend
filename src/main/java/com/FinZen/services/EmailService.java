package com.FinZen.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    /**
     * Envía un correo electrónico de notificación de inicio de sesión con HTML.
     *
     * @param to     Dirección de correo electrónico del destinatario
     * @param nombre Nombre del usuario
     */
    public void sendLoginNotification(String to, String nombre) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("FinZen - Inicio de sesión detectado");

            

            String htmlContent = "<html>" +
                    "<head>" +
                    "  <style>" +
                    "    body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f4f4f4; padding: 20px; }" +
                    "    .container { background-color: #fff; padding: 20px; border-radius: 10px; max-width: 600px; margin: auto; }" +
                    "    .header { background-color:rgb(112, 112, 112); color: white; padding: 15px; text-align: center; border-radius: 10px 10px 0 0; }" +
                    "    .content { text-align: center; padding: 20px; }" +
                    "    .footer { font-size: 12px; color: gray; text-align: center; margin-top: 20px; }" +
                    "    img { width: 150px; margin-bottom: 20px; }" +
                    "  </style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class='container'>" +
                    "    <div class='header'>" +
                    "      <h2>¡Hola, " + nombre + "!</h2>" +
                    "    </div>" +
                    "    <div class='content'>" + 
                    "      <p>Hemos detectado un <strong>inicio de sesión</strong> en tu cuenta de FinZen.</p>" +
                    "      <p><strong>Fecha y hora:</strong> " + LocalDateTime.now() + "</p>" +
                    "      <p>Si no fuiste tú,cambia tu contraseña inmediatamente.</p>" +
                    "    </div>" +
                    "    <div class='footer'>" +
                    "      <p>FinZen - Gestiona tus finanzas con facilidad.</p>" +
                    "    </div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true); // true = HTML

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envía un correo electrónico con un token para restablecer la contraseña.
     *
     * @param to     Dirección de correo electrónico del destinatario
     * @param nombre Nombre del usuario
     * @param token  Token de restablecimiento
     */
    public void sendPasswordResetToken(String to, String nombre, String token) {

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("FinZen - Restablecimiento de contraseña");

            String htmlContent = "<html>" +
                    "<style>body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f4f4f4; padding: 20px; }\" +\n" + //
                    " \"    .container { background-color: #fff; padding: 20px; border-radius: 10px; max-width: 600px; margin: auto; }\" +\n" + //
                    " \"    .header { background-color:rgb(112, 112, 112); color: white; padding: 15px; text-align: center; border-radius: 10px 10px 0 0; }\" +\n" + //
                    " \"    .content { text-align: center; padding: 20px; }\" +\n" + //
                    " \"    .footer { font-size: 12px; color: gray; text-align: center; margin-top: 20px; }\" +\n" + //
                    " \"    img { width: 150px; margin-bottom: 20px; }</style>"+
                    "<body style='font-family: Arial, sans-serif; padding: 20px;'>" +
                    "<h2>Hola " + nombre + ",</h2>" +
                    "<p>Has solicitado restablecer tu contraseña para tu cuenta de FinZen.</p>" +
                    "<p>Utiliza el siguiente código para restablecer tu contraseña:</p>" +
                    "<h3 style='color:#7d6dd9;'>" + token + "</h3>" +
                    "<p>Este código expirará en 60 minutos.</p>" +
                    "<p>Si no solicitaste este cambio, puedes ignorar este correo.</p>" +
                    "<br><p>Saludos,<br>El equipo de FinZen</p>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendRegister (String to, String nombre){
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("FinZen - Bienvenido a FinZen");

            

            String htmlContent = "<html>" +
                    "<head>" +
                    "  <style>" +
                    "    body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f4f4f4; padding: 20px; }" +
                    "    .container { background-color: #fff; padding: 20px; border-radius: 10px; max-width: 600px; margin: auto; }" +
                    "    .header { background-color:rgb(112, 112, 112); color: white; padding: 15px; text-align: center; border-radius: 10px 10px 0 0; }" +
                    "    .content { text-align: center; padding: 20px; }" +
                    "    .footer { font-size: 12px; color: gray; text-align: center; margin-top: 20px; }" +
                    "    img { width: 150px; margin-bottom: 20px; }" +
                    "  </style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class='container'>" +
                    "    <div class='header'>" +
                    "      <h2>¡Hola, " + nombre + "!</h2>" +
                    "    </div>" +
                    "    <div class='content'>" + 
                    "      <p>Gracias por elegirnos bienvenido a</strong> FinZen.</p>" +
                    "      <p><strong>Fecha y hora de registro:</strong> " + LocalDateTime.now() + "</p>" +
                    "      <p>Si no fuiste tú,cambia tu contraseña inmediatamente.</p>" +
                    "    </div>" +
                    "    <div class='footer'>" +
                    "      <p>FinZen - Gestiona tus finanzas con facilidad.</p>" +
                    "    </div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true); // true = HTML

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


}

