import { Component, OnInit } from '@angular/core';
import { TestPlanService } from '../_services/test-plans.service';
import { TestPlan } from '../Model/TestPlan';
import { Router } from '@angular/router';
import { User } from '../Model/User';
import { LocalStorageService } from '../_services/localStorage.service';
import { UserProfile } from '../Model/UserProfile';

@Component({
  selector: 'app-test-plans',
  templateUrl: './test-plans.component.html',
  styleUrls: ['./test-plans.component.css']
})
export class TestPlansComponent implements OnInit {

  constructor(
    private _testPlanService: TestPlanService,
    private router: Router,
    private _localStorage: LocalStorageService
  ) { }

  private testPlans: TestPlan[];
  private user : User;

  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._testPlanService.getTestPlans()
    .subscribe(testPlans => {
      this.testPlans = testPlans
      this.orderById()
    });
    
  }

  orderById(){
    this.testPlans.sort( (h1,h2)=> h1.id - h2.id)
  }
  
  canCreateTestPlan() : boolean{
    return UserProfile.getValueFromString(this.user.userProfile) >= UserProfile.ADMIN
  }

  createTestPlan(){
    this.router.navigate(['home/testPlan/create']);
  }

  edit(id: number){
    this.router.navigate(['home/testPlan/'+id+'/edit']);
  }

}
