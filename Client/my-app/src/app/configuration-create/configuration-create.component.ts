import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Config } from '../Model/Config';
import { ConfigService } from '../_services/config.service';

@Component({
  selector: 'app-configuration-create',
  templateUrl: './configuration-create.component.html',
  styleUrls: ['./configuration-create.component.css']
})
export class ConfigurationCreateComponent implements OnInit {

  private newConfig = new Config;

    archive ={
      expiration: "",
      period: "",
      referenceDate: ""
    }
    
    controlConnection={
      referenceDate: "",
      period: "",
      retryDelay: "",
      maxRetries: ""
    }
    
    core={
      maxSystemLogSize: "",
    	storageMonitorPeriod: "",
    	storageWarningThreshold: "",
    	storageCriticalThreshold: ""
    }

    data={
      defaultMessage: ""
    }

    download={
      retryDelay: "",
      maxRetries: ""
    }

    scanning={
      enableMonitor: false,
      enableCsq: false,
      enableMoni: false,
      enableMonp: false,
      enableSmond: false,
      enableSmonc: false,
      sampleTime: ""
    }

    server={
      serverInterface: "",
      registrationRetryDelay: "",
      serverAddress: "",
      serverUser: "",
      serverPassword: ""
    }

    testPlan={
      defaultMaxRetries: "",
      defaultRetryDelay: "",
      maxLogSize: ""
    }

    upload={
      autoUpload: false,
      referenceDate: "",
      period: "",
      retryDelay: "",
      maxRetries: "",
      maxUploadSize: ""
    }

    voice={
      defaultCallDuration: "",
      incomingCallTimeout: ""
    }
  

  constructor(
    private _location: Location,
    private _configService: ConfigService
  ) { }

  ngOnInit() {
    
  }

  goBack(){
    this._location.back();
  }

  createConfig(){
    
    if(!this.newConfig.configName){ 
      alert("You must choose a name!")
      return
    }

  
    //Atualizar properties no objecto Config
    this.newConfig.archive= this.archive
    this.newConfig.controlConnection= this.controlConnection
    this.newConfig.core= this.core
    this.newConfig.data= this.data
    this.newConfig.download= this.download
    this.newConfig.scanning= this.scanning
    this.newConfig.server= this.server
    this.newConfig.testPlan= this.testPlan
    this.newConfig.upload= this.upload
    this.newConfig.voice= this.voice

    this._configService.createConfig(this.newConfig).subscribe(
      data =>{
        //sucesso
        alert('Configuration created sucessfully')
        this.goBack()
      },
      error =>{
        alert(error.error.detail) //erro
        this.goBack()
      } 
      
    )
    
  }

}
