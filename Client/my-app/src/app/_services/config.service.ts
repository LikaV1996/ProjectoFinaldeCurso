import { Injectable } from '@angular/core';
import { Config } from '../Model/Config';
import { Configs } from '../Model/Configs';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  getConfigs(): Observable<Config[]> {
    return this.http.get<Config[]>(routes.getConfigs)
  }

  getConfigById(id: number): Observable<Config> {
    const getConfigByIdUrl = routes.getConfigById.replace(":id", id.toString());
    return this.http.get<Config>(getConfigByIdUrl)
  }
  
  createConfig(newConfig: Config) {
    
    if(newConfig.activationDate)
      newConfig.activationDate = newConfig.activationDate.toISOString().slice(0,22)
    
    if(newConfig.archive.referenceDate)
      newConfig.archive.referenceDate = newConfig.archive.referenceDate.toISOString().slice(0,22)
    
    if(newConfig.controlConnection.referenceDate)
      newConfig.controlConnection.referenceDate = newConfig.controlConnection.referenceDate.toISOString().slice(0,22)
    
    if(newConfig.upload.referenceDate)
      newConfig.upload.referenceDate = newConfig.upload.referenceDate.toISOString().slice(0,22)

    return this.http.post<Config>(routes.createConfig,
      {
        configName: newConfig.configName,
        activationDate: newConfig.activationDate,
        archive: newConfig.archive,
        controlConnection: newConfig.controlConnection,
        core: newConfig.core,
        data: newConfig.data,
        download: newConfig.download,
        scanning: newConfig.scanning,
        server: newConfig.server,
        testPlan: newConfig.testPlan,
        upload: newConfig.upload,
        voice: newConfig.voice
      })
  }

  updateConfig(config: Config): Observable<Config> {
    const updateConfig = routes.updateConfig.replace(":id", config.id.toString());

    var activDate, archiveRefDate, controlConRefDate, uploadRefDate
    if(typeof(config.activationDate)=="object")
    activDate = config.activationDate.toISOString().slice(0,22)
    else activDate = config.activationDate

    if(typeof(config.archive.referenceDate)=="object")
      archiveRefDate = config.archive.referenceDate.toISOString().slice(0,22)
    else archiveRefDate = config.archive.referenceDate

    if(typeof(config.controlConnection.referenceDate)=="object")
      controlConRefDate = config.controlConnection.referenceDate.toISOString().slice(0,22)
    else controlConRefDate = config.controlConnection.referenceDate
    
    if(typeof(config.upload.referenceDate)=="object")
      uploadRefDate = config.upload.referenceDate.toISOString().slice(0,22)
    else uploadRefDate = config.upload.referenceDate


    return this.http.put<Config>(updateConfig, {
      configName: config.configName,
      activationDate: activDate,
      archive: {expiration: config.archive.expiration, period: config.archive.period, referenceDate: archiveRefDate},
      controlConnection: { 
        referenceDate: controlConRefDate, 
        period: config.controlConnection.period,
        retryDelay: config.controlConnection.retryDelay,
        maxRetries: config.controlConnection.maxRetries
      },
      core: {
        maxSystemLogSize: config.core.maxSystemLogSize,
        storageMonitorPeriod: config.core.storageMonitorPeriod,
        storageWarningThreshold: config.core.storageWarningThreshold,
        storageCriticalThreshold: config.core.storageCriticalThreshold
      },
      data: {defaultMessage: config.data.defaultMessage},
      download: {
        retryDelay: config.download.retryDelay,
        maxRetries: config.download.maxRetries
      },
      scanning: {
        enableMonitor: config.scanning.enableMonitor,
        sampleTime: config.scanning.sampleTime,
        enableCsq: config.scanning.enableCsq,
        enableMoni: config.scanning.enableMoni,
        enableMonp: config.scanning.enableMonp,
        enableSmond: config.scanning.enableSmond,
        enableSmonc: config.scanning.enableSmonc
      },
      server: {
        serverInterface: config.server.serverInterface,
        registrationRetryDelay: config.server.registrationRetryDelay,
        serverAddress: config.server.serverAddress,
        serverUser: config.server.serverUser,
        serverPassword: config.server.serverPassword
      },
      testPlan: {
        defaultMaxRetries: config.testPlan.defaultMaxRetries,
        defaultRetryDelay: config.testPlan.defaultRetryDelay,
        maxLogSize: config.testPlan.maxLogSize
      },
      upload: {
        autoUpload: config.upload.autoUpload,
        referenceDate: uploadRefDate,
        period: config.upload.period,
        retryDelay: config.upload.retryDelay,
        maxRetries: config.upload.maxRetries,
        maxUploadSize: config.upload.maxUploadSize
      },
      voice: {
        defaultCallDuration: config.voice.defaultCallDuration,
        incomingCallTimeout: config.voice.incomingCallTimeout
      }
      })
  }

  deleteConfigByID(id: number) {
    const deleteConfigByIDUrl = routes.deleteConfig.replace(":id", id.toString());
    return this.http.delete<Config>(deleteConfigByIDUrl)
  }

}