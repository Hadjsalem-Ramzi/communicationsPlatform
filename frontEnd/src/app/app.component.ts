import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(public authService: AuthService, private router: Router) {
    this.checkAuthentication();
  }

  checkAuthentication() {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']); // Redirige vers la page de connexion si non authentifié
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']); // Redirige vers la page de connexion après déconnexion
  }
}
