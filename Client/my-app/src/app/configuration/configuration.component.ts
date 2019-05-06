import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Config } from '../Model/Config';
import { LocalStorageService } from '../_services/localStorage.service';
import { ConfigService } from '../_services/config.service';
import { User } from '../Model/User';
import { UserProfile } from '../Model/UserProfile';

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.css']
})
export class ConfigurationComponent implements OnInit {

  constructor(
    private _configService: ConfigService,
    private router: Router,
    private _localStorage: LocalStorageService
  ) { }

  private configs: Config[];
  private config : Config;
  private user : User;

  //obu_name: string;
  //obu_password: string;

  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._configService.getConfigs()
    .subscribe(configObj => {
      this.configs = configObj.configs
    });
  }

  canCreateConfig() : boolean{
    return this.user.user_level >= UserProfile.Admin
  }

  /*
  createConfig(){
    console.log("creating config")
    if(!this.obu_name || !this.obu_password){
      alert("Not all fields are filled")
    }
    else{
      this._obuService.createObu(this.obu_name, this.obu_password)
        .subscribe(obuObj => {
          this.obus.push(obuObj.obu)
        })
    }
  }
  */

  edit(id: number){
    this.router.navigate(['home/config/'+id+'/edit']);
  }

}
