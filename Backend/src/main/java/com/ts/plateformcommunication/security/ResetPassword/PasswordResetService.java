package com.ts.plateformcommunication.security.ResetPassword;

import com.ts.plateformcommunication.security.user.User;
import com.ts.plateformcommunication.security.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetService {


    private PasswordResetTokenRepository tokenRepository;


    private UserRepository userRepository;

    private JavaMailSender mailSender;

    public void createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1); // Token valide pendant 1 heure

        PasswordResetToken resetToken = new PasswordResetToken(token, expiryDate, user);
        tokenRepository.save(resetToken);

        sendResetTokenEmail(user.getEmail(), token);
    }

    private void sendResetTokenEmail(String email, String token) {
        String resetUrl = "http://your-frontend-url/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Réinitialisation du mot de passe");
        message.setText("Pour réinitialiser votre mot de passe, cliquez sur le lien suivant : " + resetUrl);

        mailSender.send(message);
    }

    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
        if (resetToken != null && !resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            User user = resetToken.getUser();
            user.setPassword(newPassword); // Assurez-vous de crypter le mot de passe avant de le sauvegarder
            userRepository.save(user);
            tokenRepository.delete(resetToken);
        }
    }
}