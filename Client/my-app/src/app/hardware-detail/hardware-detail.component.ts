import { Component, OnInit, Input } from '@angular/core';
import { Hardware } from '../Model/Hardware';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HardwareService } from '../_services/hardware.service';
import { Router, NavigationExtras } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-hardware-detail',
  templateUrl: './hardware-detail.component.html',
  styleUrls: ['./hardware-detail.component.css']
})
export class HardwareDetailComponent implements OnInit {
  @Input() hardware: Hardware;

  private id: number;

  constructor(
    private router: Router,
    private _hardwareService: HardwareService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];

    this._hardwareService.getHardwareByID(this.id).subscribe(hardware => {
      this.hardware = hardware
   })
  }

  goBack(){
    this._location.back();
  }
  
  saveChanges(){
    console.log("updating hardware")
    this.hardware.components.forEach(element => {
      element.componentType=element.componentType.toUpperCase();
    });
    this._hardwareService.updateHardware(this.hardware.id, this.hardware.serialNumber, this.hardware.components)
    .subscribe(hardware => {
      console.log('Hardware: ' + JSON.stringify(hardware))
      this.hardware = hardware
      alert("Hardware updated!")
      this.goBack()
      console.log("Hardware updated")
    })
  }

  addComponent(){
    this.hardware.components.push({
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
    this.hardware.components = this.hardware.components.filter(obj => obj !== comp);
  }

}
