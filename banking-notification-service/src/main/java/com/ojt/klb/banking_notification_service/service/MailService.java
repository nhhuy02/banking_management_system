package com.ojt.klb.banking_notification_service.service;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
@Slf4j
@Service
public class MailService {

    @Value("${email.from}")
    private String fromEmail;

    @Value("${email.password}")
    private String password;
    @Autowired
    private SpringTemplateEngine templateEngine; // Inject Thymeleaf template engine
    public void send(String email, String subject, String templateName, Map<String, Object> variables) {
        try {

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
            props.put("mail.smtp.port", "587"); // TLS Port
            props.put("mail.smtp.auth", "true"); // Enable authentication
            props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            // Set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(fromEmail, "EIGHT BANK"));
            msg.setReplyTo(InternetAddress.parse(fromEmail, false));
            msg.setSubject(subject, "UTF-8");
//            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
            // Render Thymeleaf template
            Context context = new Context();
            context.setVariables(variables); // Set variables to the context
            String body = templateEngine.process(templateName, context);

            msg.setContent(body, "text/html; charset=UTF-8");
            // Send the message
            Transport.send(msg);
            log.info("Email sent successfully!");
//            return "Email sent successfully!";
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("Failed to send email!");
//            return "Failed to send email!";
        }
    }
}
