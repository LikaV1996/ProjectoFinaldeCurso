import { Component, OnInit, Input } from '@angular/core';
import { SetupService } from '../_services/setup.service';
import { Router, NavigationExtras } from '@angular/router';
import { Location } from '@angular/common';
import { Setup } from '../Model/Setup';
import {ActivatedRoute} from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-setup-detail',
  templateUrl: './setup-detail.component.html',
  styleUrls: ['./setup-detail.component.css']
})
export class SetupDetailComponent implements OnInit {
  @Input() setup: Setup;

  private id: number;


  constructor(
    private router: Router,
    private _setupService: SetupService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }
  
  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._setupService.getSetupByID(this.id).subscribe(setup => {
      this.setup = setup
    })

  }

  goBack(){
    this._location.back();
  }

}
