import { AppService } from '../app.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
// import { NgProgress, NgProgressRef } from '@ngx-progressbar/core';
import { Subscription } from 'rxjs';

import { NgProgress, NgProgressRef } from '@ngx-progressbar/core';

@Component({
  selector: 'app-footerinfo',
  templateUrl: './footerinfo.component.html',
  styleUrls: ['./footerinfo.component.css']
})
export class FooterinfoComponent implements OnInit, OnDestroy {
  menuSubscription: Subscription;
  lastMenuCommand: String[];
  progressRef: NgProgressRef;
  constructor(public appService: AppService, private ngProgress: NgProgress) {
    this.menuSubscription = appService.menuCommand$.subscribe(
      command => {
        this.lastMenuCommand = command;
    });
   }

  ngOnInit() {
    this.progressRef = this.ngProgress.ref();
    // // Progress bar events (optional)
    // this.progressRef.started.subscribe(() => this.onStarted());
    // this.progressRef.completed.subscribe(() => this.onCompleted());
  }

  ngOnDestroy(): void {
    this.menuSubscription.unsubscribe();
  }

  // onStarted() {
  //   console.log('ProgressBar started');
  // }
  // onCompleted() {
  //   console.log('ProgressBar completed');
  // }
}
