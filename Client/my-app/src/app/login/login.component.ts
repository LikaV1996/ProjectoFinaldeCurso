import { Component, OnInit } from '@angular/core';
import { AuthService } from '../_services/auth.service'
import { LocalStorageService } from "../_services/localStorage.service";

import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"

//meus imports
import {Router, ActivatedRoute} from '@angular/router';
import { UserService } from '../_services/user.service';





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
    private _userService: UserService,
    private _localStorageService: LocalStorageService,
    private _authService: AuthService
  ) {
    /*
    let extras = router.getCurrentNavigation().extras
    if(extras != undefined && extras.state != undefined && extras.state.url != undefined)
      this.redirectUrl = extras.state.url
    */
  }

  ngOnInit() {
    this.redirectUrl = this.route.snapshot.queryParams['redirectUrl'] || '/home';
    let isLoggedIn = this._authService.isLoggedIn()
    console.log("init login, isLoggedIn=" + isLoggedIn)
    if(isLoggedIn) {
      console.log("User is already logged in")
      this.router.navigate([this.redirectUrl]);
    }
  }

  //NgModel
  username: string;
  password: string;
   

    login() {
     
      this._authService.login(this.username, this.password).subscribe(isLoggedIn => {
        //console.log("isloggedin " + isLoggedIn)
        if(isLoggedIn){
          this._userService.getUserByParam(this.username).subscribe(
            userobj => {
              let user = userobj.user
              this._localStorageService.insertCurrentUserDetails(user)

              /*
              console.log("redirectUrl in login = " + this.redirectUrl)
              let redirect = this.redirectUrl ? this.router.parseUrl(this.redirectUrl) : '/home';
 
              // Redirect the user
              this.router.navigateByUrl(redirect);
              */
             this.router.navigate([this.redirectUrl]);
            },
            err => alert("error fetching logged in user")
          )

        }
      },
      err => alert("Invalid credentials")
    )

  }


  getUserDetails(){
    return this.http.get<{user: User}>(routes.getUserByParam)
  }




}
