import { Injectable, NgModule } from '@angular/core';
import { Observable, of, throwError} from 'rxjs';
import { CommonModule } from '@angular/common';
import {
 HttpEvent,
 HttpInterceptor,
 HttpHandler,
 HttpRequest,
 HttpErrorResponse,
} from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { LocalStorageService } from "../_services/localStorage.service";
import { Routes } from "../httproutes";
import { Router } from '@angular/router';
import { AuthService } from '../_services/auth.service';
import { retry, catchError } from 'rxjs/operators';


const routes = new Routes

@Injectable()
export class HttpsRequestInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private _localStorageService: LocalStorageService,
    private _authService: AuthService
  ){}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {

    console.log("HTTP intercepted for route: " + req.url)

    //req = req.clone({ headers: req.headers.set( 'Request_Date', Date.now().toString() )})
    if (!req.headers.has('Content-Type')) {
      req = req.clone({ headers: req.headers.set( 'Content-Type', 'application/json' )})
    }

    if(req.url != routes.login) { //if login route -> don't add token
      if(this._authService.isLoggedIn()){ //check if logged in
        req = req.clone({ headers: req.headers.set( 'Authorization', this._localStorageService.getAuthToken() )});
      }
      else{
        this.router.navigate(['logout'])
      }
    }

    return next.handle(req)
        .pipe(
          //retry(1),
          catchError(this.errorHandler())
        )
  }

  errorHandler() {
    return (e: HttpErrorResponse) => {
      //debugger
      console.error("Error: " + JSON.stringify(e))
      let unknownError = false;

      console.error("ERROR_HANDLER, err = " + JSON.stringify(e))

      if(e.status == 0) {
        console.error("API not responding.")
      }
      else if(e.error.status >= 400 && e.error.status < 500) {
        unknownError = this.errorHandler4XX(e)
      }

      if(unknownError){
        console.error("Uknown error --- type: " + e.error.type + " status: " + (e.error ? e.error.status : e.status) + " \n\nfull error: " + JSON.stringify(e))
        debugger
      }
    
      return throwError(e)
    }
  }



  
  //4XX errors handler
  private errorHandler4XX(e: HttpErrorResponse) : boolean { //returns if the error was handled or if unknown
    if(e.error.status == 400) {
      if(e.error.instance == '/probs/invalid-login') {  //invalid login
        alert("Invalid Credentials")
      }
      //else if(e.error.instance == '/probs/create-user-error') {
      //  alert("Body isn't fully complete")
      //}
      
      return false
    }
    else if(e.error.status == 401) {
      //if(e.error.instance == 'user_profile-error'){
      //  alert("You are not allowed to do this action")
      //}
      
     return false
    }
    else if(e.error.status == 403){
      if(e.error.instance == '/probs/user-suspended') {  //user is suspended
        this.router.navigate(['/logout'], {state: {alertMsg: 'User is suspended'}})
        
      }
      return false
    }
    

    return true
  }


}
    
    
@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: HttpsRequestInterceptor,
    multi: true,
  },
],
})
 

export class HttpinterceptorModule { }
