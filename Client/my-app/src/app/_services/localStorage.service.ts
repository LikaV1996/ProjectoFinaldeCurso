import { Injectable } from '@angular/core';

import { User } from "../Model/User";


class localStorageToken {
    token : string;
    expirationDate : Date;
}


@Injectable({
    providedIn: 'root'
  })
export class LocalStorageService{
    constructor() {}


    private lsTokenName = "AuthToken";

    insertAuthToken(token: string) : void {
        console.log("inserting authtoken on localStorage")

        let expirationDate = new Date()
        expirationDate.setDate( (new Date()).getDate() + 1 )

        //console.log(['expirationDate in day/month/year -> ', expirationDate.getDate(), '/', expirationDate.getMonth(), '/', expirationDate.getFullYear()].join('') )
        //console.log("expirationDate in millis = " + expirationDate.getTime())

        let lsToken = new localStorageToken()
        lsToken.token = ['Basic', token].join(' ')
        lsToken.expirationDate = expirationDate

        //console.log("lsToken = " + lsToken)
        //console.log("lsToken", JSON.stringify(lsToken))

        localStorage.setItem(this.lsTokenName, JSON.stringify(lsToken))
    }

    removeAuthToken() : void {
        console.log("removing authtoken on localStorage")
        localStorage.removeItem(this.lsTokenName)
    }

    getAuthToken() : string {
        console.log("getting authtoken on localStorage")
        
        let lsTokenUnparsed = localStorage.getItem(this.lsTokenName)

        if(lsTokenUnparsed == null){
            //console.log("localStorage: " + null)
            return null
        } 

        //console.log("localStorage: " + lsTokenUnparsed)
        
        let lsToken : localStorageToken = JSON.parse( lsTokenUnparsed )
        lsToken.expirationDate = new Date(lsToken.expirationDate)
        //console.log("expiradeDateType: " + lsToken.expirationDate)
        let curDate = new Date()
        //console.log(['expirationDate in', 'day =', lsToken.expirationDate.getDate(), '|', 'month =', lsToken.expirationDate.getMonth(), '|', 'year =', lsToken.expirationDate.getFullYear()].join(' ') )
        //console.log("expirationDate in millis = " + lsToken.expirationDate.getTime())
        //console.log(['curDate in', 'day =', curDate.getDate(), '|', 'month =', curDate.getMonth(), '|', 'year =', curDate.getFullYear()].join(' ') )
        //console.log("curDate in millis = " + curDate.getTime())

        if(lsToken.expirationDate.getTime() >= curDate.getTime()) {
            return lsToken.token
        }
        else {  //token has expired, removed
            this.removeAuthToken()
            return null
        }
    }

    

    private lsUserDetailsName = "UserDetails"

    insertCurrentUserDetails(user: User) : void {
        console.log("inserting userdetails on localStorage")
        localStorage.setItem(this.lsUserDetailsName, JSON.stringify(user))
    }

    removeCurrentUserDetails() : void {
        console.log("removing userdetails on localStorage")
        localStorage.removeItem(this.lsUserDetailsName)
    }

    getCurrentUserDetails() : User {
        console.log("getting userdetails on localStorage")
        let userDetailsUnparsed = localStorage.getItem(this.lsUserDetailsName)

        return userDetailsUnparsed != null ? JSON.parse(userDetailsUnparsed) : null
    }

    /*

    checkLoggedUserSuspention() : {cleared : boolean, message : string} {
        let userDetails = this.getCurrentUserDetails()
        
        if(userDetails == null){
            return {cleared: false, message: "Something went wrong"}
        }

        return userDetails.suspended ? 
        {cleared: false, message: "User is suspended. Cannot access any resources"} :   //maybe create a you are suspended page xP
        {cleared: true, message: "User is cleared"}
    }
*/



    removeAllInfo(){
        this.removeAuthToken()
        this.removeCurrentUserDetails()
    }
}