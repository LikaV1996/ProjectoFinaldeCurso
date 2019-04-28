import { Component, OnInit } from '@angular/core';
import { User } from '../Model/User';
import { Users } from '../Model/Users';
import { UserService } from '../_services/user.service';

import {Router} from '@angular/router';
import { LocalStorageService } from '../_services/localStorage.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  //private token: string;

  constructor(
    //private router: Router,
    private _localStorage: LocalStorageService,
    private _userService: UserService
  ) {
    $('.ui.dropdown')
  .dropdown()
  ;
    /*
    const navigation = this.router.getCurrentNavigation();
    const state = navigation.extras.state as {userToken: string};
    this.token = state.userToken;
    */
   }

  private users: User[];
   private user : User;

  ngOnInit() {
    this._userService.getUsers()
    .subscribe(usersObj => {
      this.users = usersObj.users
    });

    
  }


  suspend(userID: number){
    //console.log("userID: " + userID)

    this._userService.suspendUser(userID)
    .subscribe(userObj => {
      let user = userObj.user
      let idx = this.users.findIndex( u => u.id == user.id)
      this.users[idx] = user
      //console.log("user.suspended = " + user.suspended)

      alert(`User with id ${user.id} ` + (user.suspended ? 'was suspended :(' : 'was unsuspended :)'))
    });
  }

  isActiveUser(id: number){
    if(!this.user)
      this.user = this._localStorage.getCurrentUserDetails()
    //console.log("curID: " + id + " LS ID: " + this._localStorage.getCurrentUserDetails().id)
    return this.user.id != id
  }

}
