import { Component, OnInit, ElementRef, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { Project } from '../model/project';
import { ProjectService } from '../project.service';
import { MatTableDataSource, MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';

import { ContextMenuService, ContextMenuComponent } from 'ngx-contextmenu';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-projectlist',
  templateUrl: './projectlist.component.html',
  styleUrls: ['./projectlist.component.scss'],
})

export class ProjectlistComponent implements OnInit {
  @Input() inDialog = false;
  @Output() projectSelect = new EventEmitter<Project>();
  @Output() projectOpen = new EventEmitter<Project>();

  projects: Project[];
  selectedProject: Project;

  orderBy: string;
  createdBy: number;
  sort: string;
  epp = 10;
  pageN = 1;
  status = 'Loading...';

  dataSource: MatTableDataSource<Project> = new MatTableDataSource();
  displayedColumns = ['name', 'description', 'customColumn1'];

  @ViewChild('basicMenu') public basicMenu: ContextMenuComponent;

//  @HostListener('document:click', ['$event']);

  constructor(private projectService: ProjectService, private eRef: ElementRef, private snackBar: MatSnackBar, private router: Router,
    private contextMenuService: ContextMenuService, public auth: AuthService) { }

  ngOnInit() {
    this.createdBy = this.auth.currentUser.id;
    this.refresh();
  }


  click(event) {
    if (this.eRef.nativeElement.contains(event.target)) {
      console.log('clicked inside');
    } else {
      console.log('clicked outside');
    }
  }

  editProject(p: Project): void {
    if (!p) { p = this.selectedProject; }
    this.router.navigate(['project/' + p.id]);
  }

  selectProject(p: Project) {
    this.selectedProject = p;
    this.projectSelect.emit(p);
  }

  openProject(p): void {
    if (!p) { p = this.selectedProject; }
    alert('Not Implemented');
  }

  newProject() {
    const newProject = new Project();
      newProject.coordinateSystem = 4326;
      newProject.name = 'Untitled project';
      this.projectService.addProject(newProject).subscribe(resp => {
        if (resp.status >= 200 && resp.status < 300) {
          this.projectService.getProject(resp.headers.get('Location')).subscribe( project => {
            this.router.navigate(['/project/' + project.id]);
          });
        } else { alert('Error creating project: ' + JSON.stringify(resp)); }
      },
        error => {
          // error.error = response body
          const body = JSON.parse(error.error);
          console.log(body);
          alert('Error creating project: ' + body.detail);
        }
      );
  }

  cancelOpen(): void {
  }

  onSelect(p: Project): void {
    this.selectedProject = p;
  }

  deleteProject(p: Project) {
    this.projectService.delProject(p).subscribe(
      resp => {
        if (resp.status === 200) {
          console.log('Project delete: OK');
          this.refresh();
          this.snackBar.open('Project deleted.', '', {
            duration: 2000,
          });
        } else {
          alert('Error deleting project:' + resp.statusText + ' - ' + resp.body);
        }
      }
    );
  }

  public onContextMenu($event: MouseEvent, item: any): void {
    this.contextMenuService.show.next({ event: $event, item: item });
    $event.preventDefault();
  }
  public showMessage(message: any, data?: any): void {
    alert(message + data);
  }

  refresh() {
    const filter: any = {
      size: this.epp,
      page: this.pageN
    };
    if (this.orderBy) {
      filter.orderBy = this.orderBy;
      if (this.sort == null) { this.sort = 'asc'; }
      filter.sort = this.sort;
    }
    if (this.createdBy) {
      filter.creator = this.createdBy;
    }
    this.projectService.filterAllProjects(filter).subscribe(projects  => {
      this.projects = projects;
      this.dataSource.data = projects;
      this.status = projects.length > 0 ? ('Projects found:' + projects.length) : 'No projects found.';
    },
    error => {
      this.status = 'Error getting projects:' + error.statusText;
    });
  }
}
