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

  
  createConfig(config_name: string,       /**/): Observable<Config> {//verificar ****** 
    return this.http.post<Config>(routes.createObu,{configName: config_name,      /**/})
  }

  updateConfig(id: number, conf: Config         /**/): Observable<Config> {
    const updateConfig = routes.updateConfig.replace(":id", id.toString());
    return this.http.put<Config>(updateConfig, {config: conf, /**/})
  }

  deleteConfigByID(id: number) {
    const deleteConfigByIDUrl = routes.deleteConfig.replace(":id", id.toString());
    return this.http.delete<Config>(deleteConfigByIDUrl)
  }

}