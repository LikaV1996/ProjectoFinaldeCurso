import { Component, OnInit, Input } from '@angular/core';
import { ConfigService } from '../_services/config.service';
import { Router, NavigationExtras } from '@angular/router';
import { Location } from '@angular/common';
import { Config } from '../Model/Config';
import {ActivatedRoute} from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-configuration-detail',
  templateUrl: './configuration-detail.component.html',
  styleUrls: ['./configuration-detail.component.css']
})
export class ConfigurationDetailComponent implements OnInit {
  @Input() config: Config;

  private id: number;

  constructor(
    private router: Router,
    private _configService: ConfigService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) { }


  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._configService.getConfigById(this.id).subscribe(config => {
     //console.log(config)
     this.config = config
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
