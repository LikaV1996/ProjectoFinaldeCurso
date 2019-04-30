import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { AuthService } from '../_services/auth.service';
import { Router, ActivatedRoute, NavigationExtras } from '@angular/router';

@Component({
  selector: 'app-logout',
  template: ''
})
export class LogoutComponent implements OnInit {

  private alertMsg;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private _authService: AuthService
  ) { 
    try {
      this.alertMsg = this.router.getCurrentNavigation().extras.state.alertMsg
    } catch (error) {}
  }

  ngOnInit() {
    if(this._authService.isLoggedIn()){
      this._authService.logoutProcedures() 
    }

    if(this.alertMsg){
      alert(this.alertMsg)
    }
    
    this.router.navigate(['/login'], {queryParams: {redirectUrl: this.route.snapshot.queryParams['redirectUrl']}});
  }

}
