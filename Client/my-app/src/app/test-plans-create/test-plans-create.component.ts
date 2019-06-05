import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

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
    private _location: Location
  ) { }

  ngOnInit() {
  }

  goBack(){
    this._location.back();
  }

  createConfig(){
    alert('Doing nothing yet!')
    //alert(JSON.stringify(this.properties))
  }

}
