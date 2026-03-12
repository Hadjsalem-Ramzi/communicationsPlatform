import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { PageEvent } from "@angular/material/paginator";
import { UserService } from "../services/user.service";
import { User } from "../model/User.model";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  public users: User[] = []; // Assurez-vous que c'est initialisé comme tableau
  errorMessage!: string;
  userFormGroup!: FormGroup;
  editingUser: User | null = null;
  showForm: boolean = false;
  pageSize: number = 5; // nombre d'éléments par page
  pageIndex: number = 0; // Index de la page actuelle
  displayedUsers: User[] = [];
  searchText: string = '';

  constructor(
    public userService: UserService,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.handleGetAllUsers();
    this.initUserForm();
  }

  filterUser() {
    if (this.searchText.length >= 1) {
      const filteredUsers = this.users.filter(user => user.firstname.toLowerCase().startsWith(this.searchText.toLowerCase()));
      this.displayedUsers = filteredUsers.length > 5 ? filteredUsers.slice(0, 5) : filteredUsers;
    } else {
      this.displayedUsers = [];
    }
  }

  initUserForm() {
    this.userFormGroup = this.fb.group({
      firstname: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      lastname: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      email: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
    });
  }
  handleGetAllUsers() {
    this.userService.getAllUsers(this.pageIndex, this.pageSize).subscribe({
      next: (data: any) => { // Gardez le type 'any' pour le débogage
        if (data && Array.isArray(data.content)) { // Vérifiez si 'data.content' est un tableau
          this.users = data.content;
          this.updateDisplayedUsers();
        } else {
          console.error("Données reçues :", data); // Affichez les données pour voir ce qui est retourné
          this.errorMessage = "Erreur : les données reçues ne contiennent pas de tableau d'utilisateurs.";
        }
      },
      error: (err) => {
        this.errorMessage = err;
      }
    });
  }

  updateDisplayedUsers() {
    const startIndex = this.pageSize * this.pageIndex;
    const endIndex = startIndex + this.pageSize;
    this.displayedUsers = this.users.slice(startIndex, endIndex);
  }

  handleDeleteUser(user: User) {
    if (confirm('Are you sure?') && user.id !== undefined) {
      this.userService.deleteUser(user.id).subscribe({
        next: () => {
          this.handleGetAllUsers();
        },
        error: (err) => {
          this.errorMessage = err;
        }
      });
    }
  }

  handleUserAction() {
    this.editingUser ? this.updateUser() : this.addUser();
  }

  addUser() {
    if (this.userFormGroup.valid) {
      const newUser: User = {
        firstname: this.userFormGroup.value.firstname,
        lastname: this.userFormGroup.value.lastname,
        email: this.userFormGroup.value.email,
      };

      this.userService.createUser(newUser).subscribe({
        next: () => {
          this.handleGetAllUsers();
          this.userFormGroup.reset();
          this.showForm = false;
        },
        error: (err) => {
          this.errorMessage = err;
        }
      });
    }
  }

  updateUser() {
    if (this.userFormGroup.valid && this.editingUser) {
      const updatedUser: User = {
        id: this.editingUser.id,
        firstname: this.userFormGroup.value.firstname,
        lastname: this.userFormGroup.value.lastname,
        email: this.userFormGroup.value.email,
      };

      this.userService.updateUser(updatedUser).subscribe({
        next: () => {
          this.handleGetAllUsers();
          this.userFormGroup.reset();
          this.editingUser = null;
          this.showForm = false;
        },
        error: (err) => {
          this.errorMessage = err;
        }
      });
    }
  }

  editUser(user: User) {
    this.editingUser = user;
    this.showForm = true;
    this.userFormGroup.patchValue(user);
  }

  cancelEdit() {
    this.editingUser = null;
    this.showForm = false;
    this.userFormGroup.reset();
    this.handleGetAllUsers();
  }

  handleNewUser() {
    this.editingUser = null;
    this.showForm = true;
    this.userFormGroup.reset();
  }

  getErrorMessage(fieldname: string, error: any) {
    if (error.required) {
      return `${fieldname} is required`;
    } else if (error.minlength) {
      return `${fieldname} should have at least ${error.minlength.requiredLength} characters`;
    }
    return '';
  }

  handlePageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.updateDisplayedUsers(); // Mettre à jour les utilisateurs affichés lors du changement de page
  }
}
