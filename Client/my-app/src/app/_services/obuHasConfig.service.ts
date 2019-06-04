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

  /*
  getOBUByID(id: number): Observable<OBU> {
    const getOBUByIDUrl = routes.getOBUByID.replace(":id", id.toString());

    return this.http.get<OBU>(getOBUByIDUrl)
  }

  updateObu(id: number, hardware_id: number, obuState: String, currentConfigId: number, currentTestPlanId: number, obu_name: String, obu_password: String, sims){
    const updateObuById = routes.updateObu.replace(":id", id.toString());
    return this.http.put<OBU>(updateObuById, {
      hardwareId: hardware_id, 
      obuState: obuState,
      currentConfigId: currentConfigId,
      currentTestPlanId: currentTestPlanId,
      obuName: obu_name, 
      obuPassword: obu_password,
      sims: sims
    })
  }
  */
}