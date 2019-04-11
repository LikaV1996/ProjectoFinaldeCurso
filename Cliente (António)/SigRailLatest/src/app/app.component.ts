import { Component, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { AppService } from './app.service';
import { ProjectService } from './project.service';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  titleEditable = false;
  oldTitle = '';

  constructor(public router: Router,
     private renderer: Renderer2,
     public appService: AppService,
     public projectService: ProjectService,
     public auth: AuthService
) {}

  onActivate(comp) { // Route activate
    this.projectService.log('activate:' + comp.constructor.name);
  }

  titleEdit(on) {
    this.titleEditable = on;
    if (on) {
      this.oldTitle = this.appService.currentProject.name;
      setTimeout (() => this.renderer.selectRootElement('#pName').focus(), 0);
    } else { // editing finished
      if (this.oldTitle !== this.appService.currentProject.name) {
        this.appService.menuCommand(['project', 'saveProject']);
      }
    }
  }
}
