import { Injectable } from '@angular/core';
import { UserHasOBU } from '../Model/UserHasOBU';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class UserHasObuService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  
  getUserObus(id: number): Observable<UserHasOBU[]> {
    const getUserObusByIDUrl = routes.getUserObus.replace(":id", id.toString());
    //console.log(getUserObusByIDUrl)
    return this.http.get<UserHasOBU[]>(getUserObusByIDUrl)
  }

  /*
  addConfigToObu(idObu: number, idConfig: number){
    const addConfigToObuUrl = routes.addConfigToObu.replace(":idObu", idObu.toString()).replace(":idConfig", idConfig.toString());
    return this.http.post(addConfigToObuUrl,{})
  }

  deleteConfigFromObu(idObu: number, idConfig: number){
    const deleteConfigFromObuUrl = routes.deleteConfigFromObu.replace(":idObu", idObu.toString()).replace(":idConfig", idConfig.toString());
    return this.http.delete(deleteConfigFromObuUrl,{})
  }
  */

}