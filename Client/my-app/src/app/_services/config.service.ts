import { Injectable } from '@angular/core';
import { Config } from '../Model/Config';
import { Configs } from '../Model/Configs';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  

  getConfigs(): Observable<Config[]> {
    return this.http.get<Config[]>(routes.getConfigs)
  }


  /*
  getOBUByID(id: number): Observable<{obu: OBU}> {
    const getOBUByIDUrl = routes.getOBUByID.replace(":id", id.toString());

    return this.http.get<{obu: OBU}>(getOBUByIDUrl)
  }

  createObu(obu_name: string, obu_password: string){//verificar ****** 
    return this.http.post<{obu: OBU}>(routes.createObu,{obu_name: obu_name, obu_password: obu_password, properties: "null", hardware_id:-1, current_config_id:-1,current_test_plan_id:-1 })
  }

  */

}