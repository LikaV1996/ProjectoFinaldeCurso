import { Injectable } from '@angular/core';
import { TestLog } from '../Model/TestLog';
import { SysLog } from '../Model/SysLog';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

class TestLogResp {
  fullCount : number
  listCount : number
  testLogList : TestLog[]
}

class SysLogResp {
  fullCount : number
  listCount : number
  sysLogList : SysLog[]
}

@Injectable({
  providedIn: 'root'
})
export class ObuLogService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  //http://localhost:8080/api/v1/frontoffice/obu/1/test-log?order=false&filename=file_1&page=1&limit=20
  //http://localhost:8080/api/v1/frontoffice/obu/1/system-log?order=false&filename=file_1&page=1&limit=20

  getTestLogs(obuID: number, order : boolean, pageNumber : number, pageLimit : number, filename : string) : Observable<TestLogResp> {
    let params = new HttpParams()
    params = params.append("order", (order ? "true" : "false"))
    params = params.append("page", pageNumber.toString())
    params = params.append("limit", pageLimit.toString())
    if(filename && filename != "") params = params.append("filename", filename.toString())
    
    const getTestLogsByObuID = routes.getTestLogs.replace(":id", obuID.toString())
    
    return this.http.get<TestLogResp>(getTestLogsByObuID, { params: params })
  }


  getSysLogs(obuID: number, order : boolean, pageNumber : number, pageLimit : number, filename : string) : Observable<SysLogResp> {
    let params = new HttpParams()
    params = params.append("order", (order ? "true" : "false"))
    params = params.append("page", pageNumber.toString())
    params = params.append("limit", pageLimit.toString())
    if(filename && filename != "") params = params.append("filename", filename.toString())
    
    
    const getSysLogsByObuID = routes.getSysLogs.replace(":id", obuID.toString())
    
    
    return this.http.get<SysLogResp>(getSysLogsByObuID, { params: params })
  }
  

}