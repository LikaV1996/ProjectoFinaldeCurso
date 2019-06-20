import { Component, OnInit } from '@angular/core';
import { HardwareService } from '../_services/hardware.service';
import { Hardware } from '../Model/Hardware';
import { Router } from '@angular/router';
import { User } from '../Model/User';
import { LocalStorageService } from '../_services/localStorage.service';
import { UserProfile } from '../Model/UserProfile';

@Component({
  selector: 'app-hardware',
  templateUrl: './hardware.component.html',
  styleUrls: ['./hardware.component.css']
})
export class HardwareComponent implements OnInit {

  constructor(
    private _hardwareService: HardwareService,
    private router: Router,
    private _localStorage: LocalStorageService
  ) { }

  private hardwares: Hardware[];
  private hardware : Hardware;
  private user : User;
  
  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._hardwareService.getHardwares()
    .subscribe(hardwares => {
      this.hardwares = hardwares
      this.orderById()
    });
  }

  canCreateHardware() : boolean{
    return UserProfile.getValueFromString(this.user.userProfile) >= UserProfile.ADMIN
  }

  createHardware(){
    this.router.navigate(['home/hardware/create']);
  }
  
  edit(id: number){
    this.router.navigate(['home/hardware/'+id+'/edit']);
  }

  orderById(){
    this.hardwares.sort( (h1,h2)=> h1.id - h2.id)
  }

  delete(id: number){
    this._hardwareService.deleteHardwareByID(id).subscribe(
      data =>{
        //sucesso
        console.log('Hardware deleted!', data) 
        this.hardwares = this.hardwares.filter(hardware=> hardware.id != id)
      },
      error => alert(error.error.detail) //erro
    )
  }

}
