import { Component } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LocalStorageService } from '../_services/localStorage.service';
import { Router } from '@angular/router';
import { AuthService } from '../_services/auth.service';
import { UserProfile } from '../Model/UserProfile';

@Component({
  selector: 'app-nav-menu',
  templateUrl: './nav-menu.component.html',
  styleUrls: ['./nav-menu.component.css']
})
export class NavMenuComponent {

  private display_AllUsers_Button : boolean
  private display_AllHardwares_Button : boolean
  private display_ServerLogs_Button : boolean

  constructor(
    private router: Router,
    private breakpointObserver: BreakpointObserver,
    private _authService: AuthService,
    private _localStorageService: LocalStorageService
  ) {
    this.displayButtons()
  }

  displayButtons(){
    let curUser = this._localStorageService.getCurrentUserDetails()
    this.display_AllUsers_Button = UserProfile.getValueFromString(curUser.userProfile) > UserProfile.SUPER_USER
    this.display_AllHardwares_Button = UserProfile.getValueFromString(curUser.userProfile) > UserProfile.SUPER_USER
    this.display_ServerLogs_Button = UserProfile.getValueFromString(curUser.userProfile) > UserProfile.SUPER_USER
  }

  logout(){
    this.router.navigate(['/logout']);
  }

}
