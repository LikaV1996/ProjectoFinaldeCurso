import { Component } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LocalStorageService } from '../_services/localStorage.service';
import { Router } from '@angular/router';
import { AuthService } from '../_services/auth.service';

@Component({
  selector: 'app-nav-menu',
  templateUrl: './nav-menu.component.html',
  styleUrls: ['./nav-menu.component.css']
})
export class NavMenuComponent {

  private display_AllUsers_Button : boolean

  constructor(
    private router: Router,
    private breakpointObserver: BreakpointObserver,
    private _authService: AuthService,
    private _localStorageService: LocalStorageService
  ) {
    this.displayButtons()
  }

  displayButtons(){
    console.log("sidenav things")
    let curUser = this._localStorageService.getCurrentUserDetails()
    this.display_AllUsers_Button = true//curUser.userProfile > 0
  }

  logout(){
    this.router.navigate(['/logout']);
  }

}
