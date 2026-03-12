import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError } from 'rxjs';

@Injectable({
providedIn: 'root'
})
export class AuthService {
private apiUrl = 'http://localhost:8083/PlatformCommunication/v1'; // Remplacez par l'URL réelle de votre backend

constructor(private http: HttpClient) { }

// Méthode pour se connecter
login(loginRequest: any): Observable<any> {
  return this.http.post(`${this.apiUrl}/authenticate`, loginRequest).pipe(
    catchError((error) => {
      console.error('Erreur lors de la connexion:', error);
      throw error; // Rejette l'erreur pour qu'elle soit gérée dans le composant
    })
  );
}

// Méthode pour vérifier si l'utilisateur est authentifié
isAuthenticated(): boolean {
  const token = localStorage.getItem('token');
  return token != null; // Retourne true si un token est présent
}

logout() {
  localStorage.removeItem('token');
}
}
