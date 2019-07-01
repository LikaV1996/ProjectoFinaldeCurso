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

  
  createTestPlan(newTestPlan : TestPlan){
    return this.http.post<{testPlan: TestPlan}>(routes.createTestPlan,
      {
        testplanName: newTestPlan.testplanName, 
        startDate: newTestPlan.startDate.toISOString().substring(0, 22), //Formatar para ISO
        stopDate: newTestPlan.stopDate.toISOString().substring(0, 22), 
        period: newTestPlan.period 
      })
  }

  /*
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

  deleteTestPlanByID(id: number){
    const deleteTestPlanByIDUrl = routes.deleteTestPlan.replace(":id", id.toString());
    return this.http.delete<TestPlan>(deleteTestPlanByIDUrl)
  }

}