import { Injectable } from '@angular/core';
import { OBU } from '../Model/OBU';
import { OBUs } from '../Model/OBUs';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"
import { LocalStorageService } from "./localStorage.service";


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class OBUService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  

  //Get all users from the server
  getOBUs (): Observable<OBUs> {
    return this.http.get<OBUs>(routes.getOBUs)
  }


  getOBUByID(id: number): Observable<{obu: OBU}> {
    const getOBUByIDUrl = routes.getOBUByID.replace(":id", id.toString());

    return this.http.get<{obu: OBU}>(getOBUByIDUrl)
  }

}