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
  //private properties: object;

  private properties = {
    "components" : []
  };

  //private component = new componentObj();

  component = {
    componentType: "",
    serialNumber: "",
    manufacturer: "",
    model: "",
    modemType: "",
    imei: ""
  }; 
  

  private components = [
    //this.component
  ];

  constructor(
    private router: Router,
    private _hardwareService: HardwareService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }

  ngOnInit() {
    //this.component = new componentObj
  }

  goBack(){
    this._location.back();
  }

  createHardware(){
    console.log("creating hardware")
    //console.log("COMPONENTE:  " + JSON.stringify(this.component))
    //this.properties.components.push(this.component)
    console.log("COMPONENTESSSSS:  " + JSON.stringify(this.properties))
    this._hardwareService.createHardware(this.serialNumber, this.components)//"\"components\" : [{\"serialNumber\" : \"MDBM1317392\",\"componentType\" : \"MOTHERBOARD\",\"manufacturer\" : \"Micro I/O\",\"model\" : \"MDB Monitor v1.3\"}]")
    .subscribe(hardware => {
      console.log(JSON.stringify(hardware))
      //this.obu = obu
      //this.users.push(userObj.user)
      alert("Hardware created!")
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

    /*
    this.component = {
      componentType: "",
      serialNumber: "",
      manufacturer: "",
      model: "",
      modemType: "",
      imei: ""
    }; 
    */
    console.log("components:" + JSON.stringify(this.components))
    
    //this.component = { componentType : ""}
    //this.component = new componentObj();
  }

  deleteComponent(comp){
    this.components = this.components.filter(obj => obj !== comp);
    console.log("components:" + JSON.stringify(this.components))
  }

}
