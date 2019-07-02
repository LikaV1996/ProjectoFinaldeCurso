import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Setup } from '../Model/Setup';
import { SetupService } from '../_services/setup.service';

@Component({
  selector: 'app-setup-create',
  templateUrl: './setup-create.component.html',
  styleUrls: ['./setup-create.component.css']
})
export class SetupCreateComponent implements OnInit {

  private newSetup = new Setup;
  
  private scanning = { 
    enableScanning: false,
    sampleTime: null,
    enableCsq: false,
    enableMoni: false,
    enableMonp: false,
    enableSmond: false,
    enableSmonc: false
  }

  constructor(
    private _location: Location,
    private _setupService: SetupService
  ) { }

  ngOnInit() {
  }

  goBack(){
    this._location.back();
  }

  createSetup(){

    if(!this.newSetup.setupName){ 
      alert("You must enter a Setup Name!")
      return
    }
    if(!this.newSetup.modemType || this.newSetup.modemType==""){ 
      alert("You must choose a Modem Type!")
      return
    }

    //Atualizar properties no objecto Setup
    if(this.scanning.sampleTime != null){
      if(isNaN(this.scanning.sampleTime)){ //caso sampleTime nao seja um number
        alert("Sample Time must be a number!")
        return
      }
    }
    else{
      this.scanning.sampleTime = 0
    }
    this.newSetup.scanning = this.scanning

    this._setupService.createSetup(this.newSetup)
      .subscribe(mynewsetup => {
        alert("Setup created!")
        this.goBack();
    })
    
  }

}
