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
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(routes.getUsers)
  }

  getUserByParam(param): Observable<User> {
    const getUserByParamUrl = routes.getUserByParam.replace(":param", param.toString());
    return this.http.get<User>(getUserByParamUrl)
  }

  suspendUser(id: number): Observable<User>{
    const suspendUserUrl = routes.suspendUser.replace(":id", id.toString());
    return this.http.put<User>(suspendUserUrl,null)
  }

  createUser(user_name: string, user_password: string, user_profile: string){
    return this.http.post<User>(routes.createUser,{userName: user_name, userPassword: user_password, userProfile: user_profile, properties: null})
  }

  updateUser(id:number, user_profile: string, suspended: boolean){
    const updateUserByIdUrl = routes.updateUser.replace(":id", id.toString());
    return this.http.put<User>(updateUserByIdUrl,{userProfile: user_profile, suspended: suspended})
  }

}
