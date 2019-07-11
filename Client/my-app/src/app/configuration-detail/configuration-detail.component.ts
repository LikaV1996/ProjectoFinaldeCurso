import { Component, OnInit, Input } from '@angular/core';
import { ConfigService } from '../_services/config.service';
import { Router, NavigationExtras } from '@angular/router';
import { Location } from '@angular/common';
import { Config } from '../Model/Config';
import {ActivatedRoute} from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { debug } from 'util';
import { User } from '../Model/User';
import { LocalStorageService } from '../_services/localStorage.service';

@Component({
  selector: 'app-configuration-detail',
  templateUrl: './configuration-detail.component.html',
  styleUrls: ['./configuration-detail.component.css']
})
export class ConfigurationDetailComponent implements OnInit {
  @Input() config: Config;

  private id: number;
  private user : User;
  
  constructor(
    private router: Router,
    private _configService: ConfigService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location,
    private _localStorage: LocalStorageService
  ) { }


  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.user = this._localStorage.getCurrentUserDetails()

    this._configService.getConfigById(this.id).subscribe(config => {
      this.config = config
    })

  }

  saveChanges() {
    
    let archiveExpirationTimeOptionValue = (document.getElementById("archiveExpirationTimeformat") as HTMLSelectElement).value
    this.config.archive.expiration *= parseInt(archiveExpirationTimeOptionValue)
    let archivePeriodTimeOptionValue = (document.getElementById("archivePeriodTimeformat") as HTMLSelectElement).value
    this.config.archive.period *= parseInt(archivePeriodTimeOptionValue)
    let controlConPeriodTimeOptionValue = (document.getElementById("controlConnectionPeriodTimeformat") as HTMLSelectElement).value
    this.config.controlConnection.period *= parseInt(archiveExpirationTimeOptionValue)
    let controlConRetryDelayTimeOptionValue = (document.getElementById("controlConnectionRetryDelayTimeformat") as HTMLSelectElement).value
    this.config.controlConnection.retryDelay *= parseInt(archivePeriodTimeOptionValue)
    let coreStorageMonitorPeriodTimeOptionValue = (document.getElementById("coreStorageMonitorPeriodTimeformat") as HTMLSelectElement).value
    this.config.core.storageMonitorPeriod *= parseInt(coreStorageMonitorPeriodTimeOptionValue)
    let downloadRetryDelayTimeOptionValue = (document.getElementById("downloadRetryDelayTimeformat") as HTMLSelectElement).value
    this.config.download.retryDelay *= parseInt(downloadRetryDelayTimeOptionValue)
    let scanningSampleTimeTimeOptionValue = (document.getElementById("scanningSampleTimeTimeformat") as HTMLSelectElement).value
    this.config.scanning.sampleTime *= parseInt(scanningSampleTimeTimeOptionValue)
    let serverRegRetDelayTimeTimeOptionValue = (document.getElementById("serverRegRetDelayTimeTimeformat") as HTMLSelectElement).value
    this.config.server.registrationRetryDelay *= parseInt(serverRegRetDelayTimeTimeOptionValue)
    let defRetDelTimeOptionValue = (document.getElementById("defRetDelTimeformat") as HTMLSelectElement).value
    this.config.testPlan.defaultRetryDelay *= parseInt(defRetDelTimeOptionValue)
    let uploadPeriodTimeOptionValue = (document.getElementById("uploadPeriodTimeformat") as HTMLSelectElement).value
    this.config.upload.period *= parseInt(uploadPeriodTimeOptionValue)
    let uploadRetDelTimeOptionValue = (document.getElementById("uploadRetDelTimeformat") as HTMLSelectElement).value
    this.config.upload.retryDelay *= parseInt(uploadRetDelTimeOptionValue)
    let voiceDefaultCallDurationTimeOptionValue = (document.getElementById("voiceDefaultCallDurationTimeformat") as HTMLSelectElement).value
    this.config.voice.defaultCallDuration *= parseInt(voiceDefaultCallDurationTimeOptionValue)
    let voiceIncomingCallTimeoutTimeOptionValue = (document.getElementById("voiceIncomingCallTimeoutTimeformat") as HTMLSelectElement).value
    this.config.voice.incomingCallTimeout *= parseInt(voiceIncomingCallTimeoutTimeOptionValue)

  
    this._configService.updateConfig(this.config).subscribe( config => {
      this.config = config
      alert("Config updated!")
      this.goBack()
    })
  }

  goBack(){
    this._location.back();
  }


}
