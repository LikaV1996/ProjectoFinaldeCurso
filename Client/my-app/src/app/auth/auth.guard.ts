import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../_services/auth.service';



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
    state: RouterStateSnapshot): boolean{
      let url = state.url
      console.log("AuthGuard intercepted call to url =" + url)
      //let path = url.split("localhost:4200/")

      //everytime you navigate make a request to getUserByID to get self and see suspention (in httpInterceptor)
      
      return this.checkLogin(url) && this.checkClearance(route)   
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
      else { //profile not authorized so redirect to home page
        console.log('user has no clearance')
        this.router.navigate(['/home']);
        return false
      }

    }
    else { //no clearance required to access this page
      console.log('user does not need clearance')
      return true
    }
    
  }


  async checkNotSuspended(url: string) : Promise<boolean>{
    let notSuspended : boolean = true
    await this._authService.hasSuspension().subscribe( 
        isSuspended => {
          if(isSuspended){
            this.router.navigate(['/logout'], {state: {alertMsg: 'User is suspended'}})
            
            console.log("PromiseFinished")
            notSuspended = false
          }
          /*
          else{
            notSuspended = true
          }
          */
        },
        err => console.error("error = " + JSON.stringify(err))
      )
    console.log("MethodFinished")
    return notSuspended;
  }


  
}
