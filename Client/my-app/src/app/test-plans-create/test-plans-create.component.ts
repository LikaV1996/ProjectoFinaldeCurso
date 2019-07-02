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

  //private testPlanName: string;
  //private startDate: Date;
  //private stopDate: Date;
  private newTestPlan = new TestPlan;

  /*
  properties = {
    period:"",
    setups:[]
  }
  */

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

  createTestPlan(){
    
    if(!this.newTestPlan.testplanName){ 
      alert("Name is required!")
      return
     }

     if(!this.newTestPlan.startDate || !this.newTestPlan.stopDate){ 
      alert("Date is required!")
      return
     }

     if(!this.newTestPlan.period){ 
      alert("Period is required! (Ex: P1D)")
      return
     }

    this._testPlanService.createTestPlan(this.newTestPlan)
    .subscribe(_ => {
      alert("Test Plan created!")
      this.goBack();
    })
  }

}
