import { Component, OnInit, Input } from '@angular/core';
import { OBU } from '../Model/OBU';
import { Hardware } from '../Model/Hardware';
import {ActivatedRoute} from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { OBUService } from '../_services/obu.service';
import { HardwareService } from '../_services/hardware.service';
import {Router, NavigationExtras} from '@angular/router';
import {Location} from '@angular/common';

@Component({
  selector: 'app-obu-detail',
  templateUrl: './obu-detail.component.html',
  styleUrls: ['./obu-detail.component.css']
})
export class ObuDetailComponent implements OnInit {
  @Input() obu: OBU;


  private id: number;
  private hardwares : Hardware[];

  constructor(
    private router: Router,
    private _obuService: OBUService,
    private _hardwareService: HardwareService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._obuService.getOBUByID(this.id).subscribe(obu => {
     //console.log(userObj)
     this.obu = obu
   })

   this._hardwareService.getHardwares().subscribe(hardwares =>{
    this.hardwares = hardwares
    this.hardwares.sort( (h1,h2)=> h1.id - h2.id)
    //console.log(JSON.stringify(hardwares))
  })
  }

  goBack(){
    this._location.back();
  }

  saveChanges(){
    console.log("updating obu")
    this._obuService.updateObu(this.obu.id, this.obu.hardwareId, this.obu.obuName, this.obu.properties)
    .subscribe(obu => {
      console.log(JSON.stringify(obu))
      this.obu = obu
      //this.users.push(userObj.user)
      alert("Obu updated!")
      console.log("obu updated")
    })
  }

}
