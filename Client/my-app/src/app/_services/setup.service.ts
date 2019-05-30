import { Injectable } from '@angular/core';
import { Setup } from '../Model/Setup';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"
import { stringify } from '@angular/core/src/render3/util';


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class SetupService {
  
  constructor(
    private http: HttpClient
  ) { }
    

  getSetups(): Observable<Setup[]> {
    return this.http.get<Setup[]>(routes.getSetups)
  }

  getSetupByID(id: number): Observable<Setup> {
    const getSetupByIDUrl = routes.getSetupById.replace(":id", id.toString());
    return this.http.get<Setup>(getSetupByIDUrl)
  }

  /*
  createHardware(serialNumber: string, components){
    return this.http.post<Setup>(routes.createHardware,{serialNumber: serialNumber, components: components})
  }
  
  updateHardware(id: number, serialNumber: string, components){
    const updateHardwareById = routes.updateHardware.replace(":id", id.toString());
    return this.http.put<Setup>(updateHardwareById, {serialNumber: serialNumber, components: components})
  }
  */

}