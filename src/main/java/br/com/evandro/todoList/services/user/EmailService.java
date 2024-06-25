package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String remetente;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CodeUserService codeUserService;

    private void sendSimpleMessage(String to, String subject, String text){
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

    public void sendMailToResetPassword(UUID userId){
        var user= userRepository.findById(userId).get();
        var code = codeUserService.generateResetPasswordCode(user);
        var resetUrl = "http://localhost:8080/api/auth/reset-password";
        this.sendSimpleMessage(
                user.getEmail(),
                "Password Reset",
                "This is your code for reset password: " + code.getCode() + "\n\n" +
                        "reset your password, click the link below:\n" + resetUrl
        );
    }

    public void sendMailToUserConfirmation(UUID userId){
        var user = userRepository.findById(userId).get();
        var code = codeUserService.generateConfirmationCode(user);
        var confirmationUrl = "http://localhost:8080/api/auth/user-confirmation";
        this.sendSimpleMessage(
                user.getEmail(),
                "Confirmação de usuário",
                "Esse é o seu código para confirmação de identidade de usuário: " + code + "\n" +
                        "Expira em 30 dias\n\n" +
                        "Para confirmar sua identidade, clique no link abaixo.\n" + confirmationUrl);
    }

}
