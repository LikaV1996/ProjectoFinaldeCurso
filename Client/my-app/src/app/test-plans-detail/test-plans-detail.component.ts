import { Component, OnInit, Input } from '@angular/core';
import { TestPlanService } from '../_services/test-plans.service';
import { Router, NavigationExtras } from '@angular/router';
import { Location } from '@angular/common';
import { TestPlan } from '../Model/TestPlan';
import {ActivatedRoute} from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TestPlanHasSetupService } from '../_services/test-plansHasSetup.service';
import { TestPlanHasSetup } from '../Model/TestPlanHasSetup';
import { SetupService } from '../_services/setup.service';
import { Setup } from '../Model/Setup';


@Component({
  selector: 'app-test-plans-detail',
  templateUrl: './test-plans-detail.component.html',
  styleUrls: ['./test-plans-detail.component.css']
})
export class TestPlansDetailComponent implements OnInit {
  @Input() testPlan: TestPlan;

  private id: number;
  private test_plan_has_setup: TestPlanHasSetup[];
  private setups: Setup[];
  private setupToAddId: number;

  constructor(
    private router: Router,
    private _testPlanService: TestPlanService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location,
    private _testPlanHasSetupService: TestPlanHasSetupService,
    private _setupService: SetupService
  ) { }
  
  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._testPlanService.getTestPlanByID(this.id).subscribe(testplan => {
      this.testPlan = testplan

      this._testPlanHasSetupService.getTestPlanSetups(this.id).subscribe(testplansetups =>{
        this.test_plan_has_setup = testplansetups
      })

      this._setupService.getSetups().subscribe(setups => {
        this.setups = setups
      })

    })

  }

  goBack(){
    this._location.back();
  }

  
  saveChanges(){
    this._testPlanService.updateTestPlan(this.testPlan).subscribe(t => {
      this.testPlan = t
      alert("Test Plan updated!")
      this.goBack()
    })
  }
  
 
 
 addSetupToTestPlan(testPlanId:number, setupId:number){
  if(!setupId){
    alert('You must choose a setup!')
    return
  }

  if(confirm("This will save immediately, do you want to continue?")){  
    this._testPlanHasSetupService.addSetupToTestPlan(testPlanId,setupId).subscribe(
        data =>{
          //sucesso
          alert('Setup associated sucessfully')
          this._testPlanHasSetupService.getTestPlanSetups(this.id).subscribe(tpSetups =>{
          this.test_plan_has_setup = tpSetups
        })
        },
        error => alert(error.error.detail) //erro
      )
  }
}

 deleteSetup(testPlanId:number, setupId:number){
  if(confirm("This will save immediately, do you want to continue?")){
    this._testPlanHasSetupService.deleteSetupFromTestPlan(testPlanId, setupId).subscribe(
      data =>{
        //sucesso
        alert('Setup disassociated sucessfully')
        this._testPlanHasSetupService.getTestPlanSetups(this.id).subscribe(testplansetups =>{
          this.test_plan_has_setup = testplansetups
        })
      },
      error => alert(error.error.detail) //erro
    )
  }  
}


}
