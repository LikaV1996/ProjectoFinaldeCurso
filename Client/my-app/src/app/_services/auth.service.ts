import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Routes } from "../httproutes"
import { LocalStorageService } from '../_services/localStorage.service';
import { User } from '../Model/User';


const routes = new Routes

class LoginObj{
  token : string
  user : User
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient,
    private _localStorageService: LocalStorageService
  ) { }

  //cria um observer que retorna (a quem está subscrito) se o utilizador está logado ou não
  login(username: string, password: string) : Observable<boolean>{
    
    return new Observable<boolean>(observer => {
      this.getLoginToken(username, password).subscribe( 
        loginobj => {
          //console.log("tokenobj: " + tokenobj)
          //console.log("token: " + tokenobj.token)

          const token = loginobj.token
          if(token != null){
            this._localStorageService.insertAuthToken(token)

          }

          observer.next( this.isLoggedIn() )
          observer.complete()
        },
        err => {
          observer.error(err)
        }
      )

    })
    

  }
  
  getLoginToken(username: string, password: string) : Observable<LoginObj> {
    return this.http.post<LoginObj>(routes.login, {user_name: username, user_password: password})
  }




  isLoggedIn(): boolean {
    return this._localStorageService.getAuthToken() != null
  }


  hasClearance(min_user_level : number): boolean {
    let userDetails = this._localStorageService.getCurrentUserDetails()
    console.log("authservice curClearance: " + userDetails.user_level + " || minClearance: " + min_user_level)
    
    if(!min_user_level) return true

    return userDetails && userDetails.user_level >= min_user_level
  }

/*
  hasNoSuspention(): boolean {
    console.log("authservice hasNoSuspention: " + this._localStorageService.checkLoggedUserSuspention().message)
    return this._localStorageService.checkLoggedUserSuspention().cleared
  }
*/



  logout(){
    this._localStorageService.removeAllInfo()
  }

}
