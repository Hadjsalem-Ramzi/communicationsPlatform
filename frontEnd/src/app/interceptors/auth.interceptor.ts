import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken: any = jwtDecode(token);
        const currentTime = Date.now() / 1000; // Temps actuel en secondes

        // Vérifier si le token est expiré
        if (decodedToken.exp < currentTime) {
          // Si le token a expiré, supprimer du localStorage et rediriger vers la page de connexion
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return of(); // Arrêter la requête
        }

        // Ajouter le token à l'en-tête de la requête
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      } catch (error) {
        console.error('Erreur lors de la décodification du token:', error);
        localStorage.removeItem('token');
        this.router.navigate(['/login']);
        return of(); // Arrêter la requête en cas d'erreur
      }
    }
    return next.handle(request); // Passer la requête modifiée au handler suivant
  }
}
