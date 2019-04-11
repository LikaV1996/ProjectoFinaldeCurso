/*
 * Transmite comandos do menu para os componentes da app
 */

import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Project } from './model/project';

@Injectable()
export class AppService {

  // Observable string sources
  private menuCommandSource = new Subject<string[]>();
  // Observable string streams
  menuCommand$ = this.menuCommandSource.asObservable();

  mainTitle: String;
  subTitle: String;

  currentProject: Project;

  constructor() { }

  // Service message commands
  menuCommand(action: string[]) {
    this.menuCommandSource.next(action);
  }

}
