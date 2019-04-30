import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../_services/auth.service';

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

  constructor(
    private _authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
      let url = state.url
      console.log("AuthGuard intercepted call to url =" + url)
      //let path = url.split("localhost:4200/")
      //let min_user_level = routingClearance.filter( obj => obj.url == path[path.length-1] )[0].clearance
      return this.checkLogin(url) && this.checkClearance(route)
      // && this.checkNotSuspended(url)
  }




  checkLogin(url: string): boolean {

    if (this._authService.isLoggedIn()) { return true; }
    else {
      console.log("User is not logged in")

      // Navigate to the login page with last url
      console.log('authguard queryparams: ' + url)
      this.router.navigate(['/logout'], {queryParams: {redirectUrl: url}});

      return false;
    }
  }


  checkClearance(route: ActivatedRouteSnapshot){
    console.log('this url minClearance: ' + route.data.min_user_profile )

    let min_user_profile = route.data.min_user_profile

    if (min_user_profile)
    {
      if (this._authService.hasClearance( min_user_profile ) )
      { //user has enough clearance
        console.log('user has clearance')
        return true
      }
      else
      { //profile not authorized so redirect to home page
        console.log('user has no clearance')
        this.router.navigate(['/home']);
        return false
      }

    }
    else
    { //no clearance required to access this page
      console.log('user does not need clearance')
      return true
    }
    
  }

/*
  checkNotSuspended(url: string){
    if (this._authService.hasSuspention()) {
      console.log("User is suspended")
      //this.router.navigate(['/suspended']);

      this.router.navigate(['/logout']);

      return false;
    }
    else
    {
      return true;
    }
  }
*/

  
}
