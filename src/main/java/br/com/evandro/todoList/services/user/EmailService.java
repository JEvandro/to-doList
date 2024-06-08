package br.com.evandro.todoList.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String remetente;

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text){
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(remetente);
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(text);

            mailSender.send(mail);
        } catch (Exception e) {

        }
    }

}
