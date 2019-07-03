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

  createSetup(newSetup : Setup){
    return this.http.post<{setup: Setup}>(routes.createSetup,{setupName: newSetup.setupName, modemType: newSetup.modemType, scanning: newSetup.scanning })
  }

  
  updateSetup(setup :Setup){
    const updateSetupById = routes.updateSetup.replace(":id", setup.id.toString());
    return this.http.put<Setup>(updateSetupById, 
      {
        setupName: setup.setupName, 
        modemType: setup.modemType,
        scanning: setup.scanning
      })
  }
  

  deleteSetupByID(id: number){
    const deleteSetupByIDUrl = routes.deleteSetup.replace(":id", id.toString());
    return this.http.delete<Setup>(deleteSetupByIDUrl)
  }

}