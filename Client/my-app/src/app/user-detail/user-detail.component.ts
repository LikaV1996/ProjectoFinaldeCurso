import { Component, OnInit, Input } from '@angular/core';

import {ActivatedRoute} from '@angular/router';

import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"

import {Router, NavigationExtras} from '@angular/router';
import { UserService } from '../_services/user.service';
import {Location} from '@angular/common';



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
    private http: HttpClient,
    private _location: Location
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._userService.getUserByParam(this.id).subscribe(user => {
     //console.log(userObj)
     this.user = user
   })
  }

  goBack(){
    this._location.back();
  }
  saveChanges(){
    console.log("updating user")
   
    this._userService.updateUser(this.user.id, this.user.userName, this.user.userProfile, this.user.suspended)
    .subscribe(userObj => {
      //this.users.push(userObj.user)
      console.log("user updated")
    })
  }
 

}
