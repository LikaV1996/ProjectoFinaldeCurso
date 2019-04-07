import { Component, OnInit } from '@angular/core';
import { User } from '../Model/User';
import { Users } from '../Model/Users';
import { UserService } from '../user.service';

import {Router} from '@angular/router';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  private token: string;

  constructor(
    //private router: Router,
    private userService: UserService
  ) {
    /*
    const navigation = this.router.getCurrentNavigation();
    const state = navigation.extras.state as {userToken: string};
    this.token = state.userToken;
    */
   }

  private users: User[];


  ngOnInit() {
    this.userService.getUsers(this.token)
    .subscribe(users => {
      this.users = users.users
    });

  }
  


}
