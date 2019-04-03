import { Component, OnInit, Input } from '@angular/core';

import {ActivatedRoute} from '@angular/router';

import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

let httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
    'Authorization': 'Basic dGVzdGVyOnRlc3Rlcg=='
  })
}

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  @Input() user: User;

  private id: number;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    /*
    this.route.params.subscribe( params =>
        this.id = params['id'];
    */
   this.getUser().subscribe(userObj => {
     console.log(userObj)
     this.user = userObj.user
   })
  }

  getUser() : Observable<{user: User}> {
    const getUserByIDUrl = 'http://localhost:8080/api/v1/user/' + this.id;
    //httpOptions.headers.set('Authorization', ['Basic', userToken].join(' '))
    return this.http.get<{user: User}>(getUserByIDUrl, httpOptions)
  }

}
