import { Component, OnInit } from '@angular/core';

import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

//meus imports
import {Router, NavigationExtras} from '@angular/router';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
}


class TokenObj{
  token : string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit() {
  }

  //mine
  username: string;
  password: string;

  //token: string;

    
  // Web API Urls
  private PostLoginUrl = 'http://localhost:8080/api/v1/login';
  
  //content2 = require('./users_db.json');

   //data = require('src/file.json');
   
    getLoginToken() : Observable<TokenObj> {
      return this.http.post<TokenObj>(this.PostLoginUrl, {user_name: this.username, user_password: this.password}, httpOptions)
    }

   login() {
     
     //const db = JSON.parse(this.content2);
     //console.log(users);
     
     //console.log("Json data : ", JSON.stringify(this.data));
     
     this.getLoginToken().subscribe( 
      tokenobj => {
        if(tokenobj.token != null){
          const navExtras : NavigationExtras = {state: {userToken: tokenobj.token}};
          this.router.navigate(["users"], navExtras);
        }
      },
      err => alert("Invalid credentials")
    )
  }

}
