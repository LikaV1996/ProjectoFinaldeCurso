import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { User } from './model/project';
import { Observable, throwError } from 'rxjs';

const httpHeaders = new HttpHeaders({
    'Content-Type':  'application/json',
    'Api-Key': 'key'
});

@Injectable()
export class AuthService {
  public errorMessage = '';
  private serverUrl = 'http://192.68.221.229';
  private userUrl = this.serverUrl + '/solvittool/api/user';
  public currentUser: User;
  constructor(private myRoute: Router, private http: HttpClient) {
    this.currentUser = {
      'id': 0,
      name: this.getToken(),
      username: this.getToken(),
      mail: '',
      picture: '',
      role: ''
    }
  }
  login(username, password) {
    // if (username) {
    //       this.sendToken(username);
    //       this.myRoute.navigate(['home']);
    //       return true;
    //     }
    return this.http.post(this.userUrl + '/login','', { headers: new HttpHeaders({
    'Content-Type':  'application/json',
    'Api-Key': 'key',
    'Authorization': btoa(username + ':' + password)
  }), observe: 'response', responseType: 'text'}).subscribe(
    // return this.http.get<User>(this.userUrl + '/' +  username, { headers: httpHeaders }).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          this.currentUser = {
            id: 1,
            name: 'Administrator',
            mail: 'admin@example.com',
            picture: '',
            role: '',
            username: 'admin'
          };
          this.sendToken(resp.headers.get('authorization'));
          this.myRoute.navigate(['home']);
          return true;
        }
      },
      error => {
        if (error.error instanceof ErrorEvent) {
          // A client-side or network error occurred. Handle it accordingly.
          this.errorMessage = 'An error occurred:', error.error.message;
          console.error('An error occurred:', error.error.message);
        } else {
          const err = JSON.parse(error.error);
          this.errorMessage = `Backend returned error "${err.title}", ` +
            `detail: "${err.detail}"`;
          console.error(
            `Backend returned code ${err.title}, ` +
            `detail: ${JSON.stringify(err.detail)}`);
        }
        // return an observable with a user-facing error message
        return throwError(
          'Something bad happened; please try again later.');
        }
     );
  }
  sendToken(token: string) {
    localStorage.setItem("LoggedInUser", token);
  }
  getToken() {
//    console.log('getToken:' + localStorage.getItem("LoggedInUser"));
    return localStorage.getItem("LoggedInUser");
  }
  isLoggednIn() {
    return this.getToken() !== null;
  }
  logout() {
    localStorage.removeItem("LoggedInUser");
    this.myRoute.navigate(["login"]);
  }
}