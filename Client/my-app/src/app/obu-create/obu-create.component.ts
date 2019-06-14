import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Hardware } from '../Model/Hardware';
import { HardwareService } from '../_services/hardware.service';
import { Config } from '../Model/Config';
import { ConfigService } from '../_services/config.service';
import { TestPlan } from '../Model/TestPlan';
import { TestPlanService } from '../_services/test-plans.service';
import { OBU } from '../Model/OBU';
import { OBUService } from '../_services/obu.service';

@Component({
  selector: 'app-obu-create',
  templateUrl: './obu-create.component.html',
  styleUrls: ['./obu-create.component.css']
})
export class ObuCreateComponent implements OnInit {

  private hardwares : Hardware[];
  //private configs : Config[];
  //private testPlans: TestPlan[];
  
  //private hardwareId: number;
  //private obuName: string;
  private newObu = new OBU;

  /*
  private obuState: string = "READY";
  private currentConfigId: number = null;
  private currentTestPlanId: number = null;
  private factoryConfig: number = null;
  */

  private sims = [];
  private sim = {
    modemType: "",
    msisdn: "",
    simPin: "",
    simPuk: "",
    iccid: "",
    apn: "",
    apnUser: "",
    apnPass: ""
  }; 

  

  constructor(
    private _hardwareService: HardwareService,
    private _location: Location,
    private _obuService: OBUService
    //private _configService: ConfigService,
    //private _testPlanService: TestPlanService
  ) { }

  ngOnInit() {

    this._hardwareService.getHardwares().subscribe(hardwares =>{
      this.hardwares = hardwares
      this.hardwares.sort( (h1,h2)=> h1.id - h2.id)
    })

    /*
    this._configService.getConfigs().subscribe(configs =>{
      this.configs = configs
      this.configs.sort( (h1,h2)=> h1.id - h2.id)
    })

    this._testPlanService.getTestPlans().subscribe(testplans =>{
      this.testPlans = testplans
      this.testPlans.sort( (h1,h2)=> h1.id - h2.id)
    })
    */
  }

  goBack(){
    this._location.back();
  }

  createObu(){
    //console.log("creating obu")
    
    if(!this.newObu.hardwareId){ 
      alert("You must choose an Hardware!")
      return
    }
    if(!this.newObu.obuName){ 
      alert("You must insert OBU name!")
      return
    }
     
    //Atualizar properties no objecto OBU
    if(this.sims.length != 0)
      this.newObu.sims = this.sims

    //console.log("SIMS:")
    //console.log(JSON.stringify(this.newObu.sims))
    this._obuService.createObu(this.newObu)
      .subscribe(mynewobu => {
        alert("OBU created!")
        this.goBack();
      })

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
