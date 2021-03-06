import { Injectable } from '@angular/core';
import { OBUHasConfig } from '../Model/OBUHasConfig';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class OBUHasConfigService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  
  getObuConfigs(id: number): Observable<OBUHasConfig[]> {
    const getOBUConfigsByIDUrl = routes.getObuConfigs.replace(":id", id.toString());
    console.log(getOBUConfigsByIDUrl)
    return this.http.get<OBUHasConfig[]>(getOBUConfigsByIDUrl)
  }

  addConfigToObu(idObu: number, idConfig: number){
    const addConfigToObuUrl = routes.addConfigToObu.replace(":idObu", idObu.toString()).replace(":idConfig", idConfig.toString());
    return this.http.post(addConfigToObuUrl,{})
  }

  deleteConfigFromObu(idObu: number, idConfig: number){
    const deleteConfigFromObuUrl = routes.deleteConfigFromObu.replace(":idObu", idObu.toString()).replace(":idConfig", idConfig.toString());
    return this.http.delete(deleteConfigFromObuUrl,{})
  }

}