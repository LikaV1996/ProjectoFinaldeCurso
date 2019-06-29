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

  deleteTestPlanFromObu(idObu: number, idTestPlan: number){
    const deleteTestPlanFromObuUrl = routes.deleteTestPlanFromObu.replace(":idObu", idObu.toString()).replace(":idTestPlan", idTestPlan.toString());
    return this.http.delete(deleteTestPlanFromObuUrl,{})
  }

}