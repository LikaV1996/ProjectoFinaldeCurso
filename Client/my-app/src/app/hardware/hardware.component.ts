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
    });
  }

  canCreateHardware() : boolean{
    return UserProfile.getValueFromString(this.user.userProfile) >= UserProfile.ADMIN
  }

  /* falta isto !
  createHardware(){
    console.log("creating hardware")
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
  */

  edit(id: number){
    this.router.navigate(['home/hardware/'+id+'/edit']);
  }


}
