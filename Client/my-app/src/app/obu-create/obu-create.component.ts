import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Hardware } from '../Model/Hardware';
import { HardwareService } from '../_services/hardware.service';

@Component({
  selector: 'app-obu-create',
  templateUrl: './obu-create.component.html',
  styleUrls: ['./obu-create.component.css']
})
export class ObuCreateComponent implements OnInit {

  private hardwares : Hardware[];

  private hardwareId: number;
  private obuName: string;
  private obuState: string = "READY";
  private currentConfigId: number = null;
  private currentTestPlanId: number = null;
  private factoryConfig: number = null;

  sim = {
    modemType: "",
    msisdn: "",
    simPin: "",
    simPuk: "",
    iccid: "",
    apn: "",
    apnUser: "",
    apnPass: ""
  }; 

  private sims = [ ];

  constructor(
    private _hardwareService: HardwareService,
    private _location: Location
  ) { }

  ngOnInit() {


    this._hardwareService.getHardwares().subscribe(hardwares =>{
      this.hardwares = hardwares
      this.hardwares.sort( (h1,h2)=> h1.id - h2.id)
      //console.log(JSON.stringify(hardwares))
    })
  }

  goBack(){
    this._location.back();
  }

  addSim(){

    if(this.sims.length >= 2){ //Nao pode haver mais de 2 sims
      alert('Obu can only have 2 Sims!')
      return
    }
    
    this.sims.push({
      modemType: "GSMR",
      msisdn: "",
      simPin: "",
      simPuk: "",
      iccid: "",
      apn: "",
      apnUser: "",
      apnPass: ""
    })
  }

  deleteSim(sim){
    this.sims = this.sims.filter(obj => obj !== sim);
  }

}
