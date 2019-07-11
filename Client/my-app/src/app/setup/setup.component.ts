import { Component, OnInit } from '@angular/core';
import { SetupService } from '../_services/setup.service';
import { Setup } from '../Model/Setup';
import { Router } from '@angular/router';
import { User } from '../Model/User';
import { LocalStorageService } from '../_services/localStorage.service';
import { UserProfile } from '../Model/UserProfile';

@Component({
  selector: 'app-setup',
  templateUrl: './setup.component.html',
  styleUrls: ['./setup.component.css']
})
export class SetupComponent implements OnInit {

  constructor(
    private _setupService: SetupService,
    private router: Router,
    private _localStorage: LocalStorageService
  ) { }

  private setups: Setup[];
  private user : User;


  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._setupService.getSetups()
    .subscribe(setups => {
      this.setups = setups
      this.orderById()
    });
  }

  orderById(){
    this.setups.sort( (h1,h2)=> h1.id - h2.id)
  }
  
  canCreateSetup() : boolean{
    return UserProfile.getValueFromString(this.user.userProfile) >= UserProfile.SUPER_USER
  }

  createSetup(){
    this.router.navigate(['home/setup/create']);
  }

  edit(id: number){
    this.router.navigate(['home/setup/'+id+'/edit']);
  }

  delete(id: number){
    this._setupService.deleteSetupByID(id).subscribe(
      data =>{
        //sucesso
        console.log('Setup deleted!', data) 
        this.setups = this.setups.filter(setup=> setup.id != id)
      },
      error => alert(error.error.detail) //erro
    )
  }

}
