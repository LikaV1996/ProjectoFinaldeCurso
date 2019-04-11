import { Component, OnInit, Inject } from '@angular/core';
import { ProjectService } from '../project.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Project } from '../model/project';
import { Router } from '@angular/router';

@Component({
  selector: 'app-projectlistdialog',
  templateUrl: './projectlistdialog.component.html',
  styleUrls: ['./projectlistdialog.component.css']
})
export class ProjectlistdialogComponent implements OnInit {
selectedProject: Project;
constructor(private projectService: ProjectService, private router: Router,
      public dialogRef: MatDialogRef<ProjectlistdialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {
  }

  ngOnInit() {
  }

  selectProject(p: Project) {
    this.selectedProject = p;
  }

  cancel() {
    this.dialogRef.close();
  }

  open() {
    this.router.navigate(['project/' + this.selectedProject.id]);
  }

}
