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
      console.log(id)
    const getOBUConfigsByIDUrl = routes.getObuConfigs.replace(":id", id.toString());
    console.log(getOBUConfigsByIDUrl)
    return this.http.get<OBUHasConfig[]>(getOBUConfigsByIDUrl)
  }

  /*
  getOBUByID(id: number): Observable<OBU> {
    const getOBUByIDUrl = routes.getOBUByID.replace(":id", id.toString());

    return this.http.get<OBU>(getOBUByIDUrl)
  }

  createObu(obu_name: string, obu_password: string){//verificar ****** 
    return this.http.post<{obu: OBU}>(routes.createObu,{obu_name: obu_name, obu_password: obu_password, properties: "null", hardware_id:-1, current_config_id:-1,current_test_plan_id:-1 })
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