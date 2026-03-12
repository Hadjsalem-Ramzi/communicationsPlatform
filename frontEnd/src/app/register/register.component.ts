import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { RegisterService } from "../services/register.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent implements OnInit {
  form!: FormGroup;
  isSubmitted: boolean = false;
  errorMessage: string | null = null;
  @Output() registerSuccess: EventEmitter<void> = new EventEmitter(); // Émettre un événement

  constructor(private formBuilder: FormBuilder, private registerService: RegisterService) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['', Validators.required],
    });
  }

  get formControls() {
    return this.form.controls;
  }

  onSubmit(): void {
    this.isSubmitted = true;
    this.errorMessage = null;

    if (this.form.valid) {
      this.registerService.registerUser(this.form.value).pipe(
        catchError((error) => {
          if (error.status === 409) {
            this.errorMessage = "L'email est déjà utilisé.";
          } else {
            this.errorMessage = error.error;
          }
          return of(null);
        })
      ).subscribe(response => {
        if (response) {
          console.log('Inscription réussie', response);
          this.registerSuccess.emit(); // Émet l'événement d'inscription réussie
        }
      });
    }
  }
}
