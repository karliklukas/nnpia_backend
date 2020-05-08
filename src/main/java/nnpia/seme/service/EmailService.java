package nnpia.seme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("nnpiaseme@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
            System.out.println("--email send--");
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }


}



