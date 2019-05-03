import { Component, OnInit } from '@angular/core';
import { OBUService } from '../_services/obu.service';
import { OBU } from '../Model/OBU';
import {Router} from '@angular/router';

@Component({
  selector: 'app-obu',
  templateUrl: './obu.component.html',
  styleUrls: ['./obu.component.css']
})
export class OBUComponent implements OnInit {

  constructor(
    private _obuService: OBUService,
    private router: Router
  ) { }

  private obus: OBU[];
  private obu : OBU;

  ngOnInit() {
    this._obuService.getOBUs()
    .subscribe(obuObj => {
      this.obus = obuObj.obus
    });
  }

  edit(id: number){
    this.router.navigate(['home/obu/'+id+'/edit']);
  }

}
