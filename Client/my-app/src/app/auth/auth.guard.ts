import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

/*
const routingClearance = [
  { url: "/login", clearance: 0 },
  { url: "/home/map", clearance: 0 },
  { url: "/home/users", clearance: 1 },
  { url: "/home/obus", clearance: 1 },
]
*/



@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private _authService: AuthService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
      let url = state.url
      console.log("AuthGuard intercepted call to url =" + url)
      //let path = url.split("localhost:4200/")
      //let min_user_level = routingClearance.filter( obj => obj.url == path[path.length-1] )[0].clearance
      return this.checkLogin(url) 
        //&& this.checkClearance(min_user_level) && this.checkNoSuspention()  //these should be made elsewhere!!!!!!!!!!!!! not in AuthGuard
  }

  checkLogin(url: string): boolean {

    if (this._authService.isLoggedIn()) { return true; }
    else {
      console.log("User is not logged in")
      this._authService.logout()
      // Navigate to the login page with extras
      this.router.navigate(['/login']/*, {state: {redirectUrl: url}}*/);
      return false;
    }
  }

  checkClearance(min_user_level : number){
    console.log("mul = " + min_user_level)
    if (this._authService.hasClearance(min_user_level)) { return true; }
    else {
      console.log("User has no clearance")
      this.router.navigate(['/home']);
      return false;
    }
  }

  checkNoSuspention(){
    if (this._authService.hasNoSuspention()) { return true; }
    else{
      console.log("User is suspended")
      //this.router.navigate(['/suspended']);
      this._authService.logout()
      // Navigate to the login page with extras
      this.router.navigate(['/login']/*, {state: {redirectUrl: url}}*/);
      return false;
    }
  }
  
}
