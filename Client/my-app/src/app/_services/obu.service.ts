import { Injectable } from '@angular/core';
import { OBU } from '../Model/OBU';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class OBUService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  
  getOBUs(): Observable<OBU[]> {
    return this.http.get<OBU[]>(routes.getOBUs)
  }


  getOBUByID(id: number): Observable<OBU> {
    const getOBUByIDUrl = routes.getOBUByID.replace(":id", id.toString());
    return this.http.get<OBU>(getOBUByIDUrl)
  }

  createObu(newObu : OBU){
    return this.http.post<{obu: OBU}>(routes.createObu,{obuName: newObu.obuName, hardwareId: newObu.hardwareId, sims: newObu.sims })
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

}