import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Routes } from "../httproutes"
import { LocalStorageService } from '../_services/localStorage.service';
import { User } from '../Model/User';
import { BackgroundService } from './background.service';
import { UserService } from './user.service';
import { Router } from '@angular/router';


const routes = new Routes


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private router: Router,
    private http: HttpClient,
    private _userService: UserService,
    private _backgroundService: BackgroundService,
    private _localStorageService: LocalStorageService
  ) { }


  



  //cria um observer que retorna (a quem está subscrito) se o utilizador está logado ou não
  login(username: string, password: string) : Observable<boolean>{
    
    return new Observable<boolean>(observer => {
      
      this.getLoginToken(username, password).subscribe( 
        loginObj => {

          const token = loginObj.token
          console.log("token: " + JSON.stringify(loginObj))

          if(token){
            this._localStorageService.insertAuthToken(token)

            this._userService.getUserByParam(username).subscribe(
              userObj => {
                let user = userObj.user
                
                this.loginInit(user)

                observer.next( this.isLoggedIn() )
                observer.complete()
              },
              err => observer.error(err) 
            )
          }

        },
        err => observer.error(err) 
      )

    })
  }

  private loginInit(user: User){
    //insert into localStorage
    this._localStorageService.insertCurrentUserDetails(user)
    //setup periodic update for User
    this._backgroundService.setupPeriodicUserUpdate(user.id)
  }
  
  getLoginToken(username: string, password: string) : Observable<{token: string}> {
    return this.http.post<{token: string}>(routes.login, {user_name: username, user_password: password})
  }





  isLoggedIn(): boolean {
    return this._localStorageService.getAuthToken() != null
  }


  hasClearance(min_user_level : number): boolean {
    let userDetails = this._localStorageService.getCurrentUserDetails()
    console.log("curUserClearance: " + userDetails.user_level + " vs minUserClearance: " + min_user_level)
    
    if(!min_user_level) return true

    return userDetails && userDetails.user_level >= min_user_level
  }




  logoutProcedures(){
    this._backgroundService.removePeriodicUpdates()
    this._localStorageService.removeAllInfo()
  }

}
