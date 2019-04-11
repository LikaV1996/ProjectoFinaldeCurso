import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  host: {'class': 'fill-height-or-more'}
})
export class LoginComponent implements OnInit {
  username = '';
  password = '';
  showSpinner = false;
  constructor(private myRoute: Router,
    public auth: AuthService) {
  }

  ngOnInit() {
  }

  login() {
    console.log('login');
    if (this.username && this.password) {
      this.auth.errorMessage = '';
      this.auth.login(this.username, this.password);
    } else {
      this.auth.errorMessage = "Please enter your username and password."
    }
    
    // this.auth.sendToken(this.email)
    // this.myRoute.navigate(["home"]);
  }
  skip() {
    console.log('skip login');
    this.auth.sendToken('admin');
    this.auth.currentUser = {
      name: 'Administrator',
      id: 0,
      mail: 'admin@example.com',
      picture: '',
      role: '',
      username: 'admin'
    }
    this.myRoute.navigate(["home"]);
  }
}
