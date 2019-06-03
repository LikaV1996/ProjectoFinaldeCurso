import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Hardware } from '../Model/Hardware';
import { HardwareService } from '../_services/hardware.service';
import { Config } from '../Model/Config';
import { ConfigService } from '../_services/config.service';
import { TestPlan } from '../Model/TestPlan';
import { TestPlanService } from '../_services/test-plans.service';

@Component({
  selector: 'app-obu-create',
  templateUrl: './obu-create.component.html',
  styleUrls: ['./obu-create.component.css']
})
export class ObuCreateComponent implements OnInit {

  private hardwares : Hardware[];
  private configs : Config[];
  private testPlans: TestPlan[];
  
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
    private _location: Location,
    private _configService: ConfigService,
    private _testPlanService: TestPlanService
  ) { }

  ngOnInit() {

    this._hardwareService.getHardwares().subscribe(hardwares =>{
      this.hardwares = hardwares
      this.hardwares.sort( (h1,h2)=> h1.id - h2.id)
    })

    this._configService.getConfigs().subscribe(configs =>{
      this.configs = configs
      this.configs.sort( (h1,h2)=> h1.id - h2.id)
    })

    this._testPlanService.getTestPlans().subscribe(testplans =>{
      this.testPlans = testplans
      this.testPlans.sort( (h1,h2)=> h1.id - h2.id)
    })

  }

  goBack(){
    this._location.back();
  }

  createObu(){
    alert('Doing nothing yet!')
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
