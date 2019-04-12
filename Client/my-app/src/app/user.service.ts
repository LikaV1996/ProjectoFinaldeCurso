import { Injectable } from '@angular/core';
import { User } from './Model/User';
import { Users } from './Model/Users';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "./httproutes"
import { LocalStorageService } from "./localStorage.service";


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  constructor(
    private http: HttpClient,
    private localStorageService: LocalStorageService
  ) { }
    
  

  //Get all users from the server
  getUsers (userToken: string): Observable<Users> {
    
    
    return this.http.get<Users>(routes.getUsers)
      /*
      .pipe(
        tap(_ => this.log('fetched heroes')),
        catchError(this.handleError('getHeroes', []))
      );
      */
  }

  getUserByParam(param): Observable<{user: User}> {
    const getUserByParamUrl = routes.getUserByParam.replace(":param", param.toString());

    return this.http.get<{user: User}>(getUserByParamUrl)
  }

}
