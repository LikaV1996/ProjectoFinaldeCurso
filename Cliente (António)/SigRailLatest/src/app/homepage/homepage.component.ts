import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AppService } from '../app.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {
   sidenavOpened = true;
  menuSubscription: Subscription;

  constructor(private router: Router, private appService: AppService,) {
    this.menuSubscription = appService.menuCommand$.subscribe(
      command => {
        if (command[0] === 'homepage') {
          this[command[1]]();
//          switch (command[1]) {
//            case 'CloseProject':
//              this.router.navigate(['projects']);
//          }
        }
    });
   }

  ngOnInit() {
  }

  toggleNav() {
    this.sidenavOpened = !this.sidenavOpened;
  }
}
