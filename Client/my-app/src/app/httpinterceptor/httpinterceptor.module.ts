import { Injectable, NgModule } from '@angular/core';
import { Observable, throwError} from 'rxjs';
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
    const request = req.clone({ headers: req.headers.set('Content-Type',  'application/json')})

    if(req.url == routes.login) { //login route, let through
      return next.handle(request);
    }
    else{
  
      if(this._authService.isLoggedIn()){ //check if logged in
        const dupReq = request.clone({
          headers: req.headers.set('Authorization', this._localStorageService.getAuthToken()),
        });
        return next.handle(dupReq)
        /*
        .pipe(
          retry(1),
          catchError(this.errorHandler)
        )
        */
      }
    }
  }


  errorHandler(e: HttpErrorResponse) {
    if(e.error.status == 403 && e.error.type == 'user-suspended'){
      this.router.navigate(['/logout'], {state: {alertMsg: 'User is suspended'}})
    }
    
    return throwError(e)
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
