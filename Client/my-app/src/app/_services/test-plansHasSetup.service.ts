import { Injectable } from '@angular/core';
import { TestPlanHasSetup } from '../Model/TestPlanHasSetup';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class TestPlanHasSetupService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  getTestPlanSetups(id: number): Observable<TestPlanHasSetup[]> {
    const getTestPlanSetupsByIDUrl = routes.getTestPlanSetups.replace(":id", id.toString());
    return this.http.get<TestPlanHasSetup[]>(getTestPlanSetupsByIDUrl)
  }

  addSetupToTestPlan(idTestPlan: number, idSetup: number){
    const addSetupToTestPlanUrl = routes.addSetupToTestPlan.replace(":idTestPlan", idTestPlan.toString()).replace(":idSetup", idSetup.toString());
    return this.http.post(addSetupToTestPlanUrl,{})
  }
  
  deleteSetupFromTestPlan(idTestPlan: number, idSetup: number){
    const deleteSetupFromTestPlanUrl = routes.deleteSetupFromTestPlan.replace(":idTestPlan", idTestPlan.toString()).replace(":idSetup", idSetup.toString());
    return this.http.delete(deleteSetupFromTestPlanUrl,{})
  }
  

}