import { Component } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CookieHandler } from '../cookie.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-menu',
  templateUrl: './nav-menu.component.html',
  styleUrls: ['./nav-menu.component.css']
})
export class NavMenuComponent {

  constructor(
    private router: Router,
    private breakpointObserver: BreakpointObserver,
    private cookieHandler: CookieHandler
  ) {}

  logout(){
    this.cookieHandler.removeAuthToken()
    this.router.navigate(["login"]);
  }

}
