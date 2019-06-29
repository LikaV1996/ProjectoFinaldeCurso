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
import { UserHasOBU } from '../Model/UserHasOBU';
import { UserHasObuService } from '../_services/userHasObu.service';

let routes = new Routes

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  @Input() user: User;


  private id: number;

  private user_has_obu: UserHasOBU[];

  constructor(
    private router: Router,
    private _userService: UserService,
    private _userHasObuService: UserHasObuService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._userService.getUserByParam(this.id).subscribe(user => {
     this.user = user
   })

   this._userHasObuService.getUserObus(this.id).subscribe(userobus =>{
    this.user_has_obu = userobus
  })
  }

  goBack(){
    this._location.back();
  }
  saveChanges(){
    console.log("updating user")
    this._userService.updateUser(this.user.id, this.user.userName, this.user.userProfile, this.user.suspended)
    .subscribe(user => {
      this.user = user
      console.log("user updated")
    })
  }

  deleteUserOBU(obuId:number, testPlanId:number){
    if(confirm("This will save immediately, do you want to continue?")){
      alert("TODO")
      /*
      this._obuHasTestPlanService.deleteTestPlanFromObu(obuId, testPlanId).subscribe(_ =>{
        alert('OBU disassociated sucessfully')
          this._obuHasTestPlanService.getObuTestPlans(this.id).subscribe(testplans =>{
            this.obu_has_testplans = testplans
          })
      })
      */
    }  
  }

}
