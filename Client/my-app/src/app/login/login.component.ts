import { Component, OnInit } from '@angular/core';
import { AuthService } from '../_services/auth.service'
import { LocalStorageService } from "../_services/localStorage.service";

import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"

//meus imports
import {Router, ActivatedRoute} from '@angular/router';
import { UserService } from '../_services/user.service';
import { BackgroundService } from '../_services/background.service';





const routes = new Routes

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  redirectUrl: string

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    /*
    private _userService: UserService,
    private _backgroundService: BackgroundService,
    private _localStorageService: LocalStorageService,
    */
    private _authService: AuthService
  ) {
    /*
    let extras = router.getCurrentNavigation().extras
    if(extras != undefined && extras.state != undefined && extras.state.url != undefined)
      this.redirectUrl = extras.state.url
    */
  }

  ngOnInit() {
    this.redirectUrl = this.route.snapshot.queryParams['redirectUrl'];
    if( !this.redirectUrl || this.redirectUrl.includes('logout')){
      this.redirectUrl = '/home'
    }
    
    if(this._authService.isLoggedIn()) {
      console.log("User is already logged in")
      this.router.navigate([this.redirectUrl]);
    }
    
  }

  //NgModel
  username: string;
  password: string;
   

    login() {
      if(!this.username || !this.password){ return }
     
      this._authService.login(this.username, this.password).subscribe(isLoggedIn => {
        //console.log("isloggedin " + isLoggedIn)
        if(isLoggedIn){
          
          this.router.navigate([this.redirectUrl]);

        }
      },
      err => {
        //if(err instanceof HttpErrorResponse) console.log("error is HttperrorResponse: " JSON.stringify(err))
        console.log("err: " + JSON.stringify(err))
        let e //: HttpErrorResponse = err
          = err.error

        if(e.status == 403 && e.type == 'user-suspended')
          this.router.navigate(['/logout'], {state: {alertMsg: 'User is suspended'}});
        else if(e.status == 400)// && e.type == 'login-error')
          alert("Invalid credentials")
        else {
          console.error("Something went wrong on login!!")
        }
      }
    )

  }

/*
  getUserDetails(){
    return this.http.get<{user: User}>(routes.getUserByParam)
  }
*/




}
