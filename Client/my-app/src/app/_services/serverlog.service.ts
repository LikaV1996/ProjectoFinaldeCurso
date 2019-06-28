import { Injectable } from '@angular/core';
import { ServerLog } from '../Model/ServerLog';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

class ServerLogResp {
    fullCount : number
    listCount : number
    serverLogList : ServerLog[]
}

@Injectable({
  providedIn: 'root'
})
export class ServerLogService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  //http://localhost:8080/api/v1/frontoffice/server-log?order=true&page=4&limit=20&user=tester&accessType=user

  getServerLogs(order : boolean, pageNumber : number, pageLimit : number, accessor : string, accessType : string) : Observable<ServerLogResp> {
    let params = new HttpParams()
    params = params.append("order", (order ? "true" : "false"))
    params = params.append("page", pageNumber.toString())
    params = params.append("limit", pageLimit.toString())
    if(accessor && accessor != "") params = params.append("accessor", accessor.toString())
    if(accessType && accessType != "") params = params.append("accessType", accessType.toString())
    
    return this.http.get<ServerLogResp>(routes.getServerLogs, { params: params })
  }

}