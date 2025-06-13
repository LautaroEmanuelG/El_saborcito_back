package utn.saborcito.El_saborcito_back.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarFacturaPorEmail(String destinatario, byte[] pdfBytes, String nombreArchivo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(destinatario);
            helper.setSubject("Factura - El Saborcito");
            helper.setText("Gracias por su compra. Adjunto encontrará la factura correspondiente.");

            helper.addAttachment(nombreArchivo, new org.springframework.core.io.ByteArrayResource(pdfBytes));

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo con la factura", e);
        }
    }
    public void enviarCorreoTexto(List<String> destinatarios, String asunto, String mensaje) {
        try {
            MimeMessage email = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true);
            helper.setTo(destinatarios.toArray(new String[0]));
            helper.setSubject(asunto);
            helper.setText(mensaje);
            mailSender.send(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo de prueba", e);
        }
    }

}
