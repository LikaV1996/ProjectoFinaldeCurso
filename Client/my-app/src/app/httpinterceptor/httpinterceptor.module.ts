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

import { CookieHandler } from "../cookie.service";
import { Routes } from "../routes";


const routes = new Routes

@Injectable()
export class HttpsRequestInterceptor implements HttpInterceptor {
  constructor(
    private cookieHandler: CookieHandler
  ){}

 intercept(
  req: HttpRequest<any>,
  next: HttpHandler,
 ): Observable<HttpEvent<any>> {
   const request = req.clone({ headers: req.headers.set('Content-Type',  'application/json')})

   if(req.url == routes.login)
    return next.handle(request);

    const authToken = this.cookieHandler.getAuthToken();
    if(authToken != undefined){
      const dupReq = request.clone({
        headers: req.headers.set('Authorization', authToken),
      });
      return next.handle(dupReq);
    }
    else{
      //return EMPTY
      //ERROR ???
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
