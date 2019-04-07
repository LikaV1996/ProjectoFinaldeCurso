import { Component, OnInit } from '@angular/core';
import { CookieHandler } from "../cookie.service";

import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../routes"

//meus imports
import {Router, NavigationExtras} from '@angular/router';



class TokenObj{
  token : string;
}

const routes = new Routes

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(
    private router: Router,
    private http: HttpClient,
    private cookieHandler: CookieHandler
  ) {}

  ngOnInit() {
    //this.cookieHandler.removeAuthToken()
    if(this.cookieHandler.getAuthToken() != null)
      this.router.navigate(["home"]);
  }

  //mine
  username: string;
  password: string;
  
  //content2 = require('./users_db.json');

   //data = require('src/file.json');
   
    getLoginToken() : Observable<TokenObj> {
      return this.http.post<TokenObj>(routes.login, {user_name: this.username, user_password: this.password})
    }

   login() {
     
     //const db = JSON.parse(this.content2);
     //console.log(users);
     
     //console.log("Json data : ", JSON.stringify(this.data));
     
     this.getLoginToken().subscribe( 
      tokenobj => {
        const token = tokenobj.token
        if(token != null){
          this.cookieHandler.insertAuthToken(token)
          //this._cookieService.put("AuthToken", ['Basic', tokenobj.token].join(' '));
          /*
          const navExtras : NavigationExtras = {state: {userToken: tokenobj.token}};
          this.router.navigate(["users"], navExtras);
          */
         this.router.navigate(["home"]);
        }
      },
      err => alert("Invalid credentials")
    )
  }

}
