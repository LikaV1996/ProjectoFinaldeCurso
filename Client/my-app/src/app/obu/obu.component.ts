import { Component, OnInit } from '@angular/core';
import { OBUService } from '../_services/obu.service';
import { OBU } from '../Model/OBU';
import { Router } from '@angular/router';
import { User } from '../Model/User';
import { LocalStorageService } from '../_services/localStorage.service';
import { UserProfile } from '../Model/UserProfile';

@Component({
  selector: 'app-obu',
  templateUrl: './obu.component.html',
  styleUrls: ['./obu.component.css']
})
export class OBUComponent implements OnInit {

  constructor(
    private _obuService: OBUService,
    private router: Router,
    private _localStorage: LocalStorageService
  ) { }

  private obus: OBU[];
  private obu : OBU;
  private user : User;

  obu_name: string;
  obu_password: string;

  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._obuService.getOBUs()
    .subscribe(obuObj => {
      this.obus = obuObj.obus
    });
  }

  canCreateUsers() : boolean{
    return true//this.user.user_level >= UserProfile.Admin
  }

  createObu(){
    console.log("creating obu")
    if(!this.obu_name || !this.obu_password){
      alert("Not all fields are filled")
    }
    else{
      this._obuService.createObu(this.obu_name, this.obu_password)
        .subscribe(obuObj => {
          this.obus.push(obuObj.obu)
        })
    }
  }

  edit(id: number){
    this.router.navigate(['home/obu/'+id+'/edit']);
  }

}
