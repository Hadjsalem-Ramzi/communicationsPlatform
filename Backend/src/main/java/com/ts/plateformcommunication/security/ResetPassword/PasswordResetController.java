package com.ts.plateformcommunication.security.ResetPassword;
import com.ts.plateformcommunication.security.user.User;
import com.ts.plateformcommunication.security.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ts.plateformcommunication.utils.Constants.APP_ROOT;


@RestController
@AllArgsConstructor
public class PasswordResetController {

    private PasswordResetService passwordResetService;

    private UserRepository userRepository;

    @PostMapping(APP_ROOT+"/request")
    public void requestPasswordReset(@RequestParam String email) {
        // Find user by email and create a password reset token
        User user = userRepository.findByEmail(email).get();
        passwordResetService.createPasswordResetToken(user);
    }

    @GetMapping(APP_ROOT+"/reset")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (passwordResetService.validatePasswordResetToken(token)) {
            passwordResetService.resetPassword(token, newPassword);
            return "Mot de passe réinitialisé avec succès";
        } else {
            return "Jeton de réinitialisation invalide ou expiré";
        }
    }
}