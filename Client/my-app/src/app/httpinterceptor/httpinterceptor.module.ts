import { Injectable, NgModule } from '@angular/core';
import { Observable, of, EMPTY } from 'rxjs';
import { CommonModule } from '@angular/common';
import {
 HttpEvent,
 HttpInterceptor,
 HttpHandler,
 HttpRequest,
} from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { LocalStorageService } from "../localStorage.service";
import { Routes } from "../httproutes";
import { Router } from '@angular/router';


const routes = new Routes

@Injectable()
export class HttpsRequestInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private localStorageService: LocalStorageService
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

      const authToken = this.localStorageService.getAuthToken();
  
  
      if(authToken != null){ //check if logged in
        const dupReq = request.clone({
          headers: req.headers.set('Authorization', authToken),
        });
        return next.handle(dupReq);
     }
     /*
      else{ //if not logged in, return to login page (and kill request)
        this.router.navigate(['/login'])
        return EMPTY
        //ERROR ???
      }
      */
    }
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
