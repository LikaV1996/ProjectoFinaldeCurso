import { Injectable } from '@angular/core';
import { OBU } from '../Model/OBU';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"
import { OBUStatus } from '../Model/OBUStatus';

const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class OBUService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  
  getOBUs(): Observable<OBU[]> {
    return this.http.get<OBU[]>(routes.getOBUs)
  }


  getOBUByID(id: number): Observable<OBU> {
    const getOBUByIDUrl = routes.getOBUByID.replace(":id", id.toString());
    return this.http.get<OBU>(getOBUByIDUrl)
  }

  createObu(newObu : OBU){
    return this.http.post<{obu: OBU}>(routes.createObu,{obuName: newObu.obuName, hardwareId: newObu.hardwareId, sims: newObu.sims })
  }

  updateObu(obu: OBU){
    const updateObuById = routes.updateObu.replace(":id", obu.id.toString());
    return this.http.put<OBU>(updateObuById, {
      hardwareId: obu.hardwareId,
      obuName: obu.obuName, 
      sims: obu.sims,
      uploadRequest: obu.uploadRequest,
      clearAlarmsRequest : obu.clearAlarmsRequest,
      resetRequest: obu.resetRequest,
      shutdownRequest : obu.shutdownRequest
    })
  }

  getStatusFromOBU(id: number, startDate: Date, endDate: Date): Observable<OBUStatus[]>{
    let params = new HttpParams()
    
    if(endDate) params = params.append("endDate", endDate.toISOString())
    if(startDate) params = params.append("startDate", startDate.toISOString())
    //debugger
    const getStatusFromOBUByIDUrl = routes.getStatusFromOBU.replace(":id", id.toString());
    return this.http.get<OBUStatus[]>(getStatusFromOBUByIDUrl, {params: ((endDate || startDate) ? params : null) })
  }

  deleteOBUByID(id: number) {
    const deleteOBUByIDUrl = routes.deleteOBU.replace(":id", id.toString());
    return this.http.delete<OBU>(deleteOBUByIDUrl)
  }

}