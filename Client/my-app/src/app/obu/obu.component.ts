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
    .subscribe(obus => {
      this.obus = obus
      this.orderById()
    });
  }

  canCreateObu() : boolean{
    return UserProfile.getValueFromString(this.user.userProfile) >= UserProfile.ADMIN
  }

  createObu(){
    this.router.navigate(['home/obu/create']);
  }

  /*
  edit(id: number){
    this.router.navigate(['home/obu/'+id+'/edit']);
  }
  */

  orderById(){
    this.obus.sort( (h1,h2)=> h1.id - h2.id)
  }

  delete(id: number){
    this._obuService.deleteOBUByID(id).subscribe(
      data =>{
        //sucesso
        console.log('OBU deleted!', data) 
        this.obus = this.obus.filter(obu=> obu.id != id)
      },
      error => alert(error.error.detail) //erro
    )
  }

}
