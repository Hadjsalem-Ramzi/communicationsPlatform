import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/User.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  public host: string = "http://localhost:8083/PlatformCommunication/v1/Users";

  constructor(public http: HttpClient) {}

  public getAllUsers(page: number, size: number): Observable<User[]> {
    return this.http.get<User[]>(`${this.host}/all?page=${page}&size=${size}`);
  }

  public getUserById(id: number): Observable<User> {
    return  this.http.get<User>(`${this.host}/findById/${id}`);
  }

  public getUserByName(name: string): Observable<User> {
    return this.http.get<User>(`${this.host}/findByName/${name}`);
  }

  public createUser(user: User): Observable<User> {
    return this.http.post<User>(`${this.host}/save`,user );
  }

  public updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.host}/update/${user.id}`, user);
  }

  public deleteUser(id: number): Observable<void> {
    const url = `${this.host}/delete/${id}`;
    return this.http.delete<void>(url);
  }

}
