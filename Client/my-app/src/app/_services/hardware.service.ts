import { Injectable } from '@angular/core';
import { Hardware } from '../Model/Hardware';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class HardwareService {
  
  constructor(
    private http: HttpClient
  ) { }
    

  getHardwares(): Observable<Hardware[]> {
    return this.http.get<Hardware[]>(routes.getHardwares)
  }

  getHardwareByID(id: number): Observable<Hardware> {
    const getHardwareByIDUrl = routes.getHardwareById.replace(":id", id.toString());
    return this.http.get<Hardware>(getHardwareByIDUrl)
  }

  /* to check!
  createHardware(obu_name: string, obu_password: string){//verificar ****** 
    return this.http.post<{obu: OBU}>(routes.createObu,{obu_name: obu_name, obu_password: obu_password, properties: "null", hardware_id:-1, current_config_id:-1,current_test_plan_id:-1 })
  }
  */

  /* to check!
  updateHardware(id: number, hardware_id : number ,obu_name: String, properties: String){
    const updateObuById = routes.updateObu.replace(":id", id.toString());
    return this.http.put<OBU>(updateObuById, {hardwareId: hardware_id, obuName: obu_name, properties: properties})
  }
  */

}