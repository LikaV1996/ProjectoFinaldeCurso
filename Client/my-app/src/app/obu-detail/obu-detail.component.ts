import { Component, OnInit, Input } from '@angular/core';
import { OBU } from '../Model/OBU';
import { Hardware } from '../Model/Hardware';
import {ActivatedRoute} from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { OBUService } from '../_services/obu.service';
import { HardwareService } from '../_services/hardware.service';
import { OBUHasConfigService } from '../_services/obuHasConfig.service';
import {Router, NavigationExtras} from '@angular/router';
import {Location} from '@angular/common';
import { OBUHasConfig } from '../Model/OBUHasConfig';
import { Config } from '../Model/Config';
import { ConfigService } from '../_services/config.service';
import { TestPlan } from '../Model/TestPlan';
import { TestPlanService } from '../_services/test-plans.service';

@Component({
  selector: 'app-obu-detail',
  templateUrl: './obu-detail.component.html',
  styleUrls: ['./obu-detail.component.css']
})
export class ObuDetailComponent implements OnInit {
  @Input() obu: OBU;


  private id: number;
  private hardwares : Hardware[];
  private configs : Config[];
  private testPlans: TestPlan[];
  private obu_has_configs: OBUHasConfig[];

  constructor(
    private router: Router,
    private _obuService: OBUService,
    private _hardwareService: HardwareService,
    private _configService: ConfigService,
    private _testPlanService: TestPlanService,
    private _obuHasConfigService: OBUHasConfigService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._obuService.getOBUByID(this.id).subscribe(obu => {
      this.obu = obu
      //console.log(JSON.stringify(obu))
    })

    this._hardwareService.getHardwares().subscribe(hardwares =>{
      this.hardwares = hardwares
      this.hardwares.sort( (h1,h2)=> h1.id - h2.id)
    })

    this._obuHasConfigService.getObuConfigs(this.id).subscribe(configs =>{
      this.obu_has_configs = configs
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

  saveChanges(){
    
    console.log("obu hardware")
    this._obuService.updateObu(this.obu.id, this.obu.hardwareId, this.obu.obuState, this.obu.currentConfigId, this.obu.currentTestPlanId, this.obu.obuName, this.obu.obuPassword, this.obu.sims)
    .subscribe(obu => {
      console.log('OBU: ' + JSON.stringify(obu))
      this.obu = obu
      alert("Obu updated!")
      this.goBack()
      console.log("obu updated")
    })
    
  }

  addSim(){
    if(this.obu.sims.length >= 2){ //Nao pode haver mais de 2 sims
        alert('Obu can only have 2 Sims!')
        return
    }
    this.obu.sims.push({
      modemType: "",
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
    this.obu.sims = this.obu.sims.filter(obj => obj !== sim);
  }

}
