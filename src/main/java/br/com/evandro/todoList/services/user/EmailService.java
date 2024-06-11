package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.resetpassword.ResetPasswordTokenEntity;
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
    private CodeConfirmationService codeConfirmationService;

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

    public void sendMailToForgotPassword(ResetPasswordTokenEntity resetPassword){
        var resetUrl = "http://localhost:8080/api/auth/reset-password";
        this.sendSimpleMessage(
                resetPassword.getEmail(),
                "Password Reset",
                "To reset your password, click the link below:\n" + resetUrl
        );
    }

    public void sendMailToUserConfirmation(UUID userId){
        var user = userRepository.findById(userId).get();
        var code = codeConfirmationService.generateRecoveryCode(user);
        var confirmationUrl = "http://localhost:8080/api/auth/user-confirmation";
        this.sendSimpleMessage(
                user.getEmail(),
                "Confirmation of user",
                "This is your code of confirmation of the identity of user: " + code + "\n" +
                        "Expires in ten minutes\n\n" +
                        "To confirmation your identity, click the link below:\n" + confirmationUrl);
    }

}
