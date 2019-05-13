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

  createHardware(serialNumber: string, properties: string){
    return this.http.post<Hardware>(routes.createHardware,{serialNumber: serialNumber, properties://'\"components\":[]'
      "\"components\" : [{\"serialNumber\" : \"MDBM1317392\",\"componentType\" : \"MOTHERBOARD\",\"manufacturer\" : \"Micro I/O\",\"model\" : \"MDB Monitor v1.3\"}]"
    })
  }
  

  /* to check!
  updateHardware(id: number, hardware_id : number ,obu_name: String, properties: String){
    const updateObuById = routes.updateObu.replace(":id", id.toString());
    return this.http.put<OBU>(updateObuById, {hardwareId: hardware_id, obuName: obu_name, properties: properties})
  }
  */

}