import { Component, OnInit } from '@angular/core';
import { User } from '../Model/User';
import { Users } from '../Model/Users';
import { UserService } from '../_services/user.service';

import {Router} from '@angular/router';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  //private token: string;

  constructor(
    //private router: Router,
    private _userService: UserService
  ) {
    /*
    const navigation = this.router.getCurrentNavigation();
    const state = navigation.extras.state as {userToken: string};
    this.token = state.userToken;
    */
   }

  private users: User[];


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

}
