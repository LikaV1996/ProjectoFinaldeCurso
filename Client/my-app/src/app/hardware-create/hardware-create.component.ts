import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Hardware } from '../Model/Hardware';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HardwareService } from '../_services/hardware.service';
import { Router, NavigationExtras } from '@angular/router';
import { componentHostSyntheticProperty } from '@angular/core/src/render3';

/*
class componentObj {
  //serialNumber: "serialNumber",
  public componentType: ""
  public serialNumber: ""
  public manufacturer: ""
  public model: ""
  public modemType: ""
  public imei: ""
};*/ 


@Component({
  selector: 'app-hardware-create',
  templateUrl: './hardware-create.component.html',
  styleUrls: ['./hardware-create.component.css']
})
export class HardwareCreateComponent implements OnInit {

  private serialNumber: string;

  component = {
    componentType: "",
    serialNumber: "",
    manufacturer: "",
    model: "",
    modemType: "",
    imei: ""
  }; 
  

  private components = [ ];

  constructor(
    private router: Router,
    private _hardwareService: HardwareService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }

  ngOnInit() {

  }

  goBack(){
    this._location.back();
  }

  createHardware(){
    console.log("creating hardware")
    
    if(!this.serialNumber){ 
      alert("Serial Number is required!")
      return
     }
     
    this.components.forEach(element => {
      element.componentType=element.componentType.toUpperCase();
    });
    this._hardwareService.createHardware(this.serialNumber, this.components)
    .subscribe(hardware => {
      console.log(JSON.stringify(hardware))
      alert("Hardware created!")
      this.goBack();
      console.log("Hardware created!")
    })
  }

  addComponent(){
    this.components.push({
      componentType: "",
      serialNumber: "",
      manufacturer: "",
      model: "",
      modemType: "",
      imei: ""
    })
    //console.log("components:" + JSON.stringify(this.components))
  }

  deleteComponent(comp){
    this.components = this.components.filter(obj => obj !== comp);
  }

}
