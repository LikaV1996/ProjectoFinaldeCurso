import { Component, OnInit, Input } from '@angular/core';

import {ActivatedRoute} from '@angular/router';

import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"

import {Router, NavigationExtras} from '@angular/router';
import { UserService } from '../user.service';



let routes = new Routes

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  @Input() user: User;


  private id: number;

  constructor(
    private router: Router,
    private _userService: UserService,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._userService.getUserByParam(this.id).subscribe(userObj => {
     //console.log(userObj)
     this.user = userObj.user
   })
  }

  



}
