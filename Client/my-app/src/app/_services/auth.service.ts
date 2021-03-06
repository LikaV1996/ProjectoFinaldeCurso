import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Routes } from "../httproutes"
import { LocalStorageService } from '../_services/localStorage.service';
import { User } from '../Model/User';
import { BackgroundService } from './background.service';
import { UserService } from './user.service';
import { Router } from '@angular/router';
import { UserProfile } from '../Model/UserProfile';
import { MAT_CHECKBOX_CONTROL_VALUE_ACCESSOR } from '@angular/material';


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

            //get logged in user
            this.getLoginUser().subscribe(
              user => {
                console.log(JSON.stringify(user))

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
  }
  
  getLoginToken(username: string, password: string) : Observable<{token: string}> {
    return this.http.post<{token: string}>(routes.login, {username: username, password: password})
  }

  getLoginUser() : Observable<User> {
    return this.http.get<User>(routes.loginUser)
  }



  isLoggedIn(): boolean {
    return this._localStorageService.getAuthToken() != null
  }


  hasClearance(min_user_profile : UserProfile): boolean {
    let userDetails = this._localStorageService.getCurrentUserDetails()
    console.log("curUserClearance: " + UserProfile.getValueFromString(userDetails.userProfile) + " vs minUserClearance: " + min_user_profile)
    
    if(!min_user_profile) return true

    return userDetails && UserProfile.getValueFromString(userDetails.userProfile) >= min_user_profile
  }


  hasSuspension() {
    
    this.getLoginUser()
      .subscribe( user => {
          //console.log(JSON.stringify(userObj.user))
          this._localStorageService.insertCurrentUserDetails(user)
        },
        err => {
          //error handled in httpinterceptor
        })
        
  }




  logoutProcedures(){
    //this._backgroundService.removePeriodicUpdates()
    this._localStorageService.removeAllInfo()
  }

}
