import { Component, OnInit, Input } from '@angular/core';
import { CookieHandler } from "../cookie.service";

import {ActivatedRoute} from '@angular/router';

import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../routes"

//logout
import {Router, NavigationExtras} from '@angular/router';


let httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
}

let routes = new Routes

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  @Input() user: User;

  private cookie: String;

  private id: number;

  constructor(
    //logout
    private router: Router,

    private route: ActivatedRoute,
    private http: HttpClient,
    private cookieHandler: CookieHandler
  ) {
    this.cookie = cookieHandler.getAuthToken()
  }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    /*
    this.route.params.subscribe( params =>
        this.id = params['id'];
    */
   this.getUser().subscribe(userObj => {
     //console.log(userObj)
     this.user = userObj.user
   })
  }

  getUser() : Observable<{user: User}> {
    const getUserByIDUrl = routes.getUserByID.replace(":id", this.id.toString());
    //httpOptions.headers.set('Authorization', ['Basic', userToken].join(' '))

    
    return this.http.get<{user: User}>(getUserByIDUrl)
  }





/*
  logout(){
    this.cookieHandler.removeAuthToken()
    this.router.navigate(["login"]);
  }
  */
}
