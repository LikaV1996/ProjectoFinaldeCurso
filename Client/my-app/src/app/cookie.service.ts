import { Injectable } from '@angular/core';
import { CookieService } from "angular2-cookie/core";

import { HttpClient, HttpHeaders } from '@angular/common/http';



/*
const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
      'Authorization': 'Basic dGVzdGVyOnRlc3Rlcg=='
    })
  }
*/



const cookieName = "AuthToken";

@Injectable({
    providedIn: 'root'
  })
export class CookieHandler{
    constructor(
        private _cookieService: CookieService
    )
    {}

    /*
    insertCookie(key: string, value: string){
        this._cookieService.put(key, value);
    }
    */

    insertAuthToken(token: string){
        console.log("inserting authtoken cookie")
        this._cookieService.put(cookieName, ['Basic', token].join(' '));
    }

    removeAuthToken(){
        console.log("removing authtoken cookie")
        this._cookieService.remove(cookieName)
    }

    getAuthToken(){
        console.log("getting authtoken cookie")
        return this._cookieService.get(cookieName)
    }

    /*
    getHttpOptions(){
        const token = this._cookieService.get(cookieName)
        if (token == null)
            return {
                headers: new HttpHeaders({
                    'Content-Type': 'application/json'
                })
            }
        else
            return {
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                    'Authorization': token
                })
            }
    }
    */

}