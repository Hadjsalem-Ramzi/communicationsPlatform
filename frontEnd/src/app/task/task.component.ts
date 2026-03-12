import { Component, OnInit } from '@angular/core';
import { TaskService } from '../services/task.service';
import { ProjectService } from '../services/project.service';
import { UserService } from '../services/user.service';
import { Task } from '../model/Tache.model';
import { Project } from '../model/Project.model';
import { User } from '../model/User.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PageEvent } from "@angular/material/paginator";

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {
  public tasks: Task[] = [];
  public filteredTasks: Task[] = []; // Stocke les tâches filtrées
  public projects: Project[] = [];
  public users: User[] = [];
  page: number = 0; // Valeur de la page (à définir selon votre logique)
  size: number = 10;
  errorMessage!: string;
  taskFormGroup!: FormGroup;
  editingTask: Task | null = null;
  showForm: boolean = false;
  pageSize: number = 5;
  pageIndex: number = 0;
  displayedTasks: Task[] = [];
  searchText: string = '';

  constructor(
    private taskService: TaskService,
    private projectService: ProjectService,
    private userService: UserService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.handleGetAllTasks();
    this.initTaskForm();
    this.loadProjects();
    this.loadUsers();
    this.editingTask = null;
  }

  // Récupérer la liste des projets via serviceProject
  loadProjects() {
    this.projectService.getAllProjects().subscribe({
      next: (data: Project[]) => {
        this.projects = data;
      },
      error: (err) => {
        this.errorMessage = `Erreur lors du chargement des projets : ${err}`;
      }
    });
  }

  // Récupérer la liste des utilisateurs via serviceUser
  loadUsers() {
    this.userService.getAllUsers( this.page, this.size).subscribe({
      next: (data: User[]) => {
        this.users = data;
      },
      error: (err) => {
        this.errorMessage = `Erreur lors du chargement des utilisateurs : ${err}`;
      }
    });
  }

  initTaskForm() {
    this.taskFormGroup = this.fb.group({
      name: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      contenu: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      user: this.fb.control(null, Validators.required),
      project: this.fb.control(null, Validators.required),
    });
  }

  handleGetAllTasks() {
    this.taskService.getAllTasks().subscribe({
      next: (data: Task[]) => {
        this.tasks = data;
        this.filteredTasks = data; // Initialiser filteredTasks
        this.updateDisplayedTasks();
      },
      error: (err) => {
        this.errorMessage = err;
      }
    });
  }

  updateDisplayedTasks() {
    const startIndex = this.pageSize * this.pageIndex;
    const endIndex = startIndex + this.pageSize;
    this.displayedTasks = this.filteredTasks.slice(startIndex, endIndex);
  }

  handleTaskAction() {
    if (this.editingTask) {
      this.updateTask();
    } else {
      this.addTask();
    }
  }

  addTask() {
    if (this.taskFormGroup.valid) {
      const newTask: Task = {
        name: this.taskFormGroup.value.name,
        contenu: this.taskFormGroup.value.contenu,
        user: this.taskFormGroup.value.user,
        project: this.taskFormGroup.value.project,
      };
      const projectId = this.taskFormGroup.value.project.id;
      const userId = this.taskFormGroup.value.user.id;
      this.taskService.createTask(newTask, projectId, userId).subscribe({
        next: () => {
          this.handleGetAllTasks();
          this.taskFormGroup.reset();
          this.showForm = false;
        },
        error: (err) => {
          this.errorMessage = err;
        }
      });
    }
  }

  updateTask() {
    if (this.taskFormGroup.valid && this.editingTask) {
      const updatedTask: Task = {
        id: this.editingTask.id,
        name: this.taskFormGroup.value.name,
        contenu: this.taskFormGroup.value.contenu,
        user: this.taskFormGroup.value.user,
        project: this.taskFormGroup.value.project
      };

      const projectId = this.taskFormGroup.value.project?.id || 0; // Add fallback value
      const userId = this.taskFormGroup.value.user?.id || 0;       // Add fallback value

      this.taskService.updateTask(updatedTask, this.editingTask.id, projectId, userId).subscribe({
        next: () => {
          this.handleGetAllTasks();
          this.taskFormGroup.reset();
          this.editingTask = null;
          this.showForm = false;
        },
        error: (err) => {
          this.errorMessage = err;
        }
      });
    }
  }

  editTask(task: Task) {
    this.editingTask = task;
    this.showForm = true;
    this.taskFormGroup.patchValue({
      name: task.name,
      contenu: task.contenu,
      user: task.user,
      project: task.project
    });
  }

  cancelEdit() {
    this.editingTask = null;
    this.showForm = false;
    this.taskFormGroup.reset();
    this.handleGetAllTasks();
  }

  handleNewTask() {
    this.editingTask = null;
    this.showForm = true;
    this.displayedTasks = [];
  }

  handlePageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.updateDisplayedTasks();
  }

  filterTasks() {
    if (this.searchText) {
      this.filteredTasks = this.tasks.filter(task =>
        task.name.toLowerCase().includes(this.searchText.toLowerCase()) ||
        task.contenu.toLowerCase().includes(this.searchText.toLowerCase()) ||
        task.project.name.toLowerCase().includes(this.searchText.toLowerCase()) ||
        task.user.firstname.toLowerCase().includes(this.searchText.toLowerCase())
      );
    } else {
      this.filteredTasks = this.tasks;
    }
    this.updateDisplayedTasks(); // Update displayed tasks after filtering
  }

  handleDeleteTache(task: Task) {
    if (confirm(`Are you sure you want to delete the task: ${task.name}?`)) {
      this.taskService.deleteTask(task.id).subscribe({
        next: () => {
          this.handleGetAllTasks(); // Refresh tasks after deletion
        },
        error: (err) => {
          this.errorMessage = err;
        }
      });
    }
  }
}
