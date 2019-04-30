import { Injectable } from '@angular/core';
import { LocalStorageService } from './localStorage.service';
import { UserService } from './user.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class BackgroundService {

  constructor(
    private router: Router,
    private _userService: UserService,
    private _localStorageService: LocalStorageService
  ) { }


  private userInterval;
  private userPeriod = 5 * 60 * 1000 //5 minutes
                       //30 * 1000 //30 seconds

  /*
  private tokenExpirationInterval;
  private tokenExpirationPeriod = //5 * 60 * 1000 //5 minutes
                                  60 * 1000 //1 min
  private tokenExpirationStart;
  */

  setupPeriodicUserUpdate(userID: number){

    console.log("started periodicUserUpdate")
    this.userInterval = setInterval(
      () => {
        this._userService.getUserByParam(userID).subscribe(
          userObj => {
            let user = userObj.user

            this._localStorageService.insertCurrentUserDetails(user)
            console.log("Updated localStorage user")

          },
          err => {
            if(err.error.status == 403 && err.error.type == 'user-suspended'){
              this.logoutWithAlert("It seems you have been suspended")
            }
            else{
              console.error("unexpected error updating user!!!!")
            }
          }
        )
        
      },
      this.userPeriod
    )
  }

/*
  setupPeriodicTokenCheck(){

    console.log("started periodicTokenCheck")
    this.userInterval = setInterval(
      () => {
        
        
        
      },
      this.tokenExpirationInterval
    )

  }

  removeTokenPeriodicUpdate(){
    console.log("stopped periodicTokenCheck")
    clearInterval(this.tokenExpirationInterval)
  }
  */



  removePeriodicUserUpdate(){
    console.log("stopped periodicUserUpdate")
    clearInterval(this.userInterval)
  }

  removePeriodicUpdates(){
    this.removePeriodicUserUpdate()
  }



  private logoutWithAlert(alertMsg: string){
    this.router.navigate(['/logout'], {state: {alertMsg: alertMsg}});
  }
}
