import { Injectable } from '@angular/core';
import { Hardware } from '../Model/Hardware';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"
import { stringify } from '@angular/core/src/render3/util';


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

  createHardware(serialNumber: string, components){
    return this.http.post<Hardware>(routes.createHardware,{serialNumber: serialNumber, components: components})
}
  
  updateHardware(id: number, serialNumber: string, components){
    const updateHardwareById = routes.updateHardware.replace(":id", id.toString());
    return this.http.put<Hardware>(updateHardwareById, {serialNumber: serialNumber, components: components})
  }

  deleteHardwareByID(id: number){
    const deleteHardwareByIDUrl = routes.deleteHardware.replace(":id", id.toString());
    return this.http.delete<Hardware>(deleteHardwareByIDUrl)
  }
  

}