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
     console.log(JSON.stringify(hardware))

   })
  }

  goBack(){
    this._location.back();
  }

  /* falta fazer quando a rota de update tiver boa!
  saveChanges(){
    console.log("updating hardware")
    this._hardwareService.updateHardware(this.obu.id, this.obu.hardwareId, this.obu.obuName, this.obu.properties)
    .subscribe(obu => {
      console.log(JSON.stringify(obu))
      this.obu = obu
      //this.users.push(userObj.user)
      alert("Obu updated!")
      console.log("obu updated")
    })
  }
  */

}
