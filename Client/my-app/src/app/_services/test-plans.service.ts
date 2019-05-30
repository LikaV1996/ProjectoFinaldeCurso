import { Injectable } from '@angular/core';
import { TestPlan } from '../Model/TestPlan';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class TestPlanService {
  
  constructor(
    private http: HttpClient
  ) { }


  getTestPlans(): Observable<TestPlan[]> {
    return this.http.get<TestPlan[]>(routes.getTestPlans)
  }

  getTestPlanByID(id: number): Observable<TestPlan> {
    const getTestPlanByIDUrl = routes.getTestPlanById.replace(":id", id.toString());
    return this.http.get<TestPlan>(getTestPlanByIDUrl)
  }

  /*
  createTestPlan(obu_name: string, obu_password: string){//verificar ****** 
    return this.http.post<{obu: OBU}>(routes.createObu,{obu_name: obu_name, obu_password: obu_password, properties: "null", hardware_id:-1, current_config_id:-1,current_test_plan_id:-1 })
  }

  updateTestPlan(id: number, hardware_id: number, obuState: String, currentConfigId: number, currentTestPlanId: number, obu_name: String, obu_password: String, sims){
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