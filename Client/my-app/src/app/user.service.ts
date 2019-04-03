import { Injectable } from '@angular/core';
import { User } from './Model/User';
import { Users } from './Model/Users';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})


export class UserService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  // Web API Urls
  private GetUsersUrl = 'http://localhost:8080/api/v1/users';
 

  //Get all users from the server
  getUsers (userToken: string): Observable<Users> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': ['Basic', userToken].join(' ')
      })
    }
    
    return this.http.get<Users>(this.GetUsersUrl, httpOptions)
      /*
      .pipe(
        tap(_ => this.log('fetched heroes')),
        catchError(this.handleError('getHeroes', []))
      );
      */
  }

}
