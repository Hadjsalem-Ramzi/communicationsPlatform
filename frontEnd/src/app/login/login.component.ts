import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
  sub: string;
  exp: number;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string | null = null;
  showRegisterForm = false;
  successMessage: string | null = null; // Propriété pour le message de succès

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const loginRequest = {
        email: this.loginForm.value.email,
        password: this.loginForm.value.password
      };

      this.authService.login(loginRequest).subscribe(
        (response: any) => {
          if (response && response.access_token) {
            const token = response.access_token;

            // Vérification si le token est expiré
            const decodedToken: DecodedToken = jwtDecode(token);
            const currentTime = Date.now() / 1000; // Temps actuel en secondes

            if (decodedToken.exp < currentTime) {
              // Si le token a expiré, supprimer du localStorage
              this.errorMessage = 'Le token est expiré, veuillez vous reconnecter.';
              return;
            }

            // Sauvegarder le token dans le localStorage
            localStorage.setItem('token', token);
            console.log(decodedToken);
            this.router.navigate(['/task']);
          } else {
            this.errorMessage = 'Erreur lors de la connexion. Token invalide.';
          }
        },
        (error) => {
          this.errorMessage = 'Nom d\'utilisateur ou mot de passe incorrect.';
          console.error('Login error:', error);
        }
      );
    }
  }

  toggleForm() {
    this.showRegisterForm = !this.showRegisterForm;
  }

  onRegisterSuccess() {
    this.successMessage = "Registered successfully! Please log in."; // Message de succès
    this.showRegisterForm = false; // Afficher le formulaire de connexion
  }
}
