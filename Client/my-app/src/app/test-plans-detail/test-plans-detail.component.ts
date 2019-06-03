import { Component, OnInit, Input } from '@angular/core';
import { TestPlanService } from '../_services/test-plans.service';
import { Router, NavigationExtras } from '@angular/router';
import { Location } from '@angular/common';
import { TestPlan } from '../Model/TestPlan';
import {ActivatedRoute} from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-test-plans-detail',
  templateUrl: './test-plans-detail.component.html',
  styleUrls: ['./test-plans-detail.component.css']
})
export class TestPlansDetailComponent implements OnInit {
  @Input() testPlan: TestPlan;

  private id: number;

  constructor(
    private router: Router,
    private _testPlanService: TestPlanService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }
  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._testPlanService.getTestPlanByID(this.id).subscribe(testplan => {
      this.testPlan = testplan
    })

  }
  goBack(){
    this._location.back();
  }

  /*
  saveChanges(){
    console.log("updating config")
    this._configService.updateConfig(this.obu.id, this.obu.hardwareId, this.obu.obuName, this.obu.properties)
    .subscribe(obu => {
      console.log(JSON.stringify(obu))
      this.obu = obu
      //this.users.push(userObj.user)
      alert("Configuration updated!")
      console.log("config updated")
    })
  }
  */
 
}
