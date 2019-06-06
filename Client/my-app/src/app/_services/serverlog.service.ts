import { Injectable } from '@angular/core';
import { ServerLog } from '../Model/ServerLog';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  getServerLogs(order : boolean, pageNumber : number, pageLimit : number, username : string, accessType : string) : Observable<ServerLogResp> {
    const queryParams = ( !(order || pageNumber || pageLimit || username || accessType) ? "" :
        "?" + 
        (order ? "order="+order+"&" : "") +
        (pageNumber ? "page="+pageNumber+"&" : "") + 
        (pageLimit ? "limit="+pageLimit+"&" : "") + 
        (username ? "user="+username+"&" : "") + 
        (accessType ? "accessType="+accessType+"&" : "")
    )

    console.log("queryParams: " + queryParams)
    return this.http.get<ServerLogResp>(routes.getServerLogs + queryParams)
  }

}