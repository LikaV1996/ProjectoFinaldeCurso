import { Injectable } from '@angular/core';
import { OBUHasTestPlan } from '../Model/OBUHasTestPlan';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class OBUHasTestPlanService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  
  getObuTestPlans(id: number): Observable<OBUHasTestPlan[]> { 
    const getOBUTestPlansByIDUrl = routes.getObuTestPlans.replace(":id", id.toString()); 
    return this.http.get<OBUHasTestPlan[]>(getOBUTestPlansByIDUrl)
  }


  addTestPlanToObu(idObu: number, idTestPlan: number){
    const addTestPlanToObuUrl = routes.addTestPlanToObu.replace(":idObu", idObu.toString()).replace(":idTestPlan", idTestPlan.toString());
    return this.http.post(addTestPlanToObuUrl,{})
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