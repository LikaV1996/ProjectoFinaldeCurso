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

  
  updateTestPlan(testPlan: TestPlan){
    const updatetestPlanById = routes.updateTestPlan.replace(":id", testPlan.id.toString());
    return this.http.put<TestPlan>(updatetestPlanById, {
      testplanName: testPlan.testplanName, 
      startDate: testPlan.startDate.toISOString().slice(0,22),
      stopDate: testPlan.stopDate.toISOString().slice(0,22),
      period: testPlan.period,
    })
  }
  

  deleteTestPlanByID(id: number){
    const deleteTestPlanByIDUrl = routes.deleteTestPlan.replace(":id", id.toString());
    return this.http.delete<TestPlan>(deleteTestPlanByIDUrl)
  }

}