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

  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._configService.getConfigs()
    .subscribe(config => {
      this.configs = config
    });
  }

  canCreateConfig() : boolean{
    return UserProfile.getValueFromString(this.user.userProfile) >= UserProfile.ADMIN
  }

  
  createConfig(){
    this.router.navigate(['home/config/create']);
  }
  

  edit(id: number){
    this.router.navigate(['home/config/'+id+'/edit']);
  }

}
