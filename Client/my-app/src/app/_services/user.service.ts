import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Users } from '../Model/Users';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"


const routes = new Routes

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  constructor(
    private http: HttpClient
  ) { }
    
  

  //Get all users from the server
  getUsers(): Observable<Users> {
    
    
    return this.http.get<Users>(routes.getUsers)
  }

  getUserByParam(param): Observable<{user: User}> {
    const getUserByParamUrl = routes.getUserByParam.replace(":param", param.toString());

    return this.http.get<{user: User}>(getUserByParamUrl)
  }

  suspendUser(id: number): Observable<{user: User}>{
    const suspendUserUrl = routes.suspendUser.replace(":id", id.toString());

    return this.http.put<{user: User}>(suspendUserUrl,null)
  }

  createUser(user_name: string, user_password: string/*, user_profile: string*/){
    return this.http.post<{user: User}>(routes.createUser,{user_name: user_name, user_password: user_password/*, user_profile: user_profile*/, properties: null})
  }

}
