import { Injectable } from '@angular/core';
import { UserHasOBU } from '../Model/UserHasOBU';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class UserHasObuService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  getUserObus(id: number): Observable<UserHasOBU[]> {
    const getUserObusByIDUrl = routes.getUserObus.replace(":id", id.toString());
    return this.http.get<UserHasOBU[]>(getUserObusByIDUrl)
  }

  
  addObuToUser(idObu: number, idUser: number, role:string){
    return this.http.post(routes.addObuToUser,{userID: idUser, obuID: idObu, role: role})
  }

  deleteObuFromUser(idObu: number, idUser: number){
    const deleteObuFromUserUrl = routes.deleteObuFromUser.replace(":idObu", idObu.toString()).replace(":idUser", idUser.toString());
    return this.http.delete(deleteObuFromUserUrl,{})
  }
  

}