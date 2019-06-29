import { Component, OnInit } from '@angular/core';
import { User } from '../Model/User';
import { Users } from '../Model/Users';
import { UserService } from '../_services/user.service';

import {Router} from '@angular/router';
import { LocalStorageService } from '../_services/localStorage.service';
import { UserProfile } from '../Model/UserProfile';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  //private token: string;

  constructor(
    private router: Router,
    private _localStorage: LocalStorageService,
    private _userService: UserService
  ) {
    /*
    const navigation = this.router.getCurrentNavigation();
    const state = navigation.extras.state as {userToken: string};
    this.token = state.userToken;
    */
   }

  private users: User[];
  private user : User;

  private loggedInUserProfile: number;


  user_name: string;
  user_password: string;
  user_profile: string;

  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()
    this.user_profile = 'NORMAL_USER'
    this.loggedInUserProfile = UserProfile.getValueFromString(this.user.userProfile)

    this._userService.getUsers()
    .subscribe(users => {
      this.users = users
    });

  }

  suspend(userID: number){
    //console.log("userID: " + userID)
    this._userService.suspendUser(userID)
    .subscribe(user => {
      let idx = this.users.findIndex( u => u.id == user.id)
      this.users[idx] = user
      //console.log("user.suspended = " + user.suspended)

      alert(`User with id ${user.id} ` + (user.suspended ? 'was suspended :(' : 'was unsuspended :)'))
    });
  }


  createUser(){
    console.log("creating user")
    if(!this.user_name || !this.user_password){
      alert("Not all fields are filled")
    }
    else{
      this._userService.createUser(this.user_name, this.user_password, this.user_profile)
        .subscribe(user => {
          this.users.push(user)
          alert("User created!")
          this.user_name = ''
          this.user_password = ''
          this.user_profile = 'NORMAL_USER'
        })
    }
  }

  canCreateUsers() : boolean {
    return this.loggedInUserProfile >= UserProfile.ADMIN
  }

  canEditUsers(id: number) : boolean {
    let idx = this.users.findIndex( u => u.id == id)
    let curUserProfile = UserProfile.getValueFromString(this.users[idx].userProfile)

    return this.user.id == id
    || this.loggedInUserProfile > curUserProfile
  }

  canBeSuspended(id: number) : boolean{  //function for displaying (or not) the "suspend" button
    let idx = this.users.findIndex( u => u.id == id)
    let curUserProfile = UserProfile.getValueFromString(this.users[idx].userProfile)

    return this.user.id != id 
    && this.loggedInUserProfile > curUserProfile
  }
  

  edit(id: number){
    this.router.navigate(['home/user/'+id+'/edit']);
  }
  

}
