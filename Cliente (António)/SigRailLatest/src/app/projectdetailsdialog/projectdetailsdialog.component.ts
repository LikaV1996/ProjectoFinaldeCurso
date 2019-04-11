import { Project } from '../model/project';
import { ProjectService } from '../project.service';
import { Component, OnInit, Input, SimpleChanges, OnChanges, Inject } from '@angular/core';
import { MatCard, MatSnackBar } from '@angular/material';
import { MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-projectdetailsdialog',
  templateUrl: './projectdetailsdialog.component.html',
  styleUrls: ['./projectdetailsdialog.component.scss']
})
export class ProjectdetailsdialogComponent implements OnInit, OnChanges {
  @Input() project: Project;
  @Input() projectDetails: any;
  @Input() isNew = false;

  currentTab = 0;

  constructor(private projectService: ProjectService,
      private snackBar: MatSnackBar,
      public dialogRef: MatDialogRef<ProjectdetailsdialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: Project) {
  }

  public _project: Project = new Project();

  ngOnInit() {
    this.project = this.data[0];
    this.projectDetails = this.data[1];
    this.getDetails();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.getDetails();
  }

  getTitle() {
    return this.project.id > 0 ? 'Project properties' : 'New Project';
  }

  getDetails() {
    this._project.name = this.project.name;
    this._project.comment = this.project.comment;
    this._project.coordinateSystem = this.project.coordinateSystem;
  }

  setDetails() {
    this.project.name = this._project.name;
    this.project.comment = this._project.comment;
    this.project.coordinateSystem = this._project.coordinateSystem;
  }

  saveProject() {
    this.setDetails();
    this.dialogRef.close('OK');
  }

  cancel() {
    this.dialogRef.close();
  }

}
