import { Component, OnInit } from '@angular/core';
import { OBUService } from '../_services/obu.service';
import { OBU } from '../Model/Obu';

@Component({
  selector: 'app-obu',
  templateUrl: './obu.component.html',
  styleUrls: ['./obu.component.css']
})
export class OBUComponent implements OnInit {

  constructor(
    private _obuService: OBUService
  ) { }

  private obus: OBU[];

  ngOnInit() {
    this._obuService.getOBUs()
    .subscribe(obuObj => {
      this.obus = obuObj.OBUs
    });
  }

}
