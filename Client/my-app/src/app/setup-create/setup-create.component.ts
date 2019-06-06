import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-setup-create',
  templateUrl: './setup-create.component.html',
  styleUrls: ['./setup-create.component.css']
})
export class SetupCreateComponent implements OnInit {


  private setupName: string;
  private modemType = "GSMR";
  scanning = { 
    enableScanning: "",
    sampleTime: "",
    enableCsq: "",
    enableMoni: "",
    enableMonp: "",
    enableSmond: "",
    enableSmonc: ""
  }

  constructor(
    private _location: Location
  ) { }

  ngOnInit() {
  }

  goBack(){
    this._location.back();
  }

  createSetup(){
    alert('Doing nothing yet!')
    //alert(JSON.stringify(this.properties))
  }

}
