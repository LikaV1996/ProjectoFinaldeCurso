import { Component, OnInit } from '@angular/core';

//meus imports
import {Router} from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

  //mine
  username: string;
  password: string;

  //content2 = require('./users_db.json');

   //data = require('src/file.json');
   
   login() : void {
     
     //const db = JSON.parse(this.content2);
     //console.log(users);
     
     //console.log("Json data : ", JSON.stringify(this.data));
    
    
    if(this.username == 'admin' && this.password == 'admin'){
     this.router.navigate(["user"]);
     
    }else {
      alert("Invalid credentials");
    }
    
  }


}
