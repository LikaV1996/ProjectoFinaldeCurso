import { Component, OnInit, Input } from '@angular/core';
import { OBU } from '../Model/OBU';
import {ActivatedRoute} from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { OBUService } from '../_services/obu.service';
import {Router, NavigationExtras} from '@angular/router';

@Component({
  selector: 'app-obu-detail',
  templateUrl: './obu-detail.component.html',
  styleUrls: ['./obu-detail.component.css']
})
export class ObuDetailComponent implements OnInit {
  @Input() user: OBU;


  private id: number;

  constructor(
    private router: Router,
    private _obuService: OBUService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }

  ngOnInit() {
  }

}
