import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { TestPlanService } from '../_services/test-plans.service';
import { TestPlan } from '../Model/TestPlan';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-test-plans-create',
  templateUrl: './test-plans-create.component.html',
  styleUrls: ['./test-plans-create.component.css']
})
export class TestPlansCreateComponent implements OnInit {

  private testPlanName: string;
  private startDate: Date;
  private stopDate: Date;

  properties = {
    period:"",
    setups:[]
  }

  constructor(
    private router: Router,
    private _testPlanService: TestPlanService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }


  ngOnInit() {
    
  }

  goBack(){
    this._location.back();
  }

  createConfig(){
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

}
