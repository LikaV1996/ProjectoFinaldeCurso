import { Railway } from '../model/project';
import { ProjectService } from '../project.service';
import { Component, OnInit, Input, OnChanges, Inject, SimpleChanges, Output } from '@angular/core';
import { MatSnackBar, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Feature, Point } from 'geojson';

@Component({
  selector: 'app-pointeditdialog',
  templateUrl: './pointeditdialog.component.html',
  styleUrls: ['./pointeditdialog.component.css']
})
export class PointeditdialogComponent  implements OnInit, OnChanges {
  @Input() point: Feature<Point>;

  _point: Feature<Point>;

  constructor(private projectService: ProjectService,
      private snackBar: MatSnackBar,
      public dialogRef: MatDialogRef<PointeditdialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {
  }

  ngOnInit() {
    this.point = this.data;
    this._point = JSON.parse(JSON.stringify(this.point));
  }

  ngOnChanges(changes: SimpleChanges) {
//    this.getDetails();
  }

  getDetails() {
    this._point.geometry.coordinates[0] = this.point.geometry.coordinates[0];
    this._point.geometry.coordinates[1] = this.point.geometry.coordinates[1];
    this._point.geometry.coordinates[2] = this.point.geometry.coordinates[2];
    this._point.properties.kilometricPoint = this.point.properties.kilometricPoint;
  }

  setDetails() {
    this.point.geometry.coordinates[0] = this._point.geometry.coordinates[0];
    this.point.geometry.coordinates[1] = this._point.geometry.coordinates[1];
    this.point.geometry.coordinates[2] = this._point.geometry.coordinates[2];
    this.point.properties.kilometricPoint = this._point.properties.kilometricPoint;
  }

  apply() {
    this.setDetails();
    this.dialogRef.close('OK');
  }

  cancel() {
    console.log('cancel');
    this.dialogRef.close();
  }

}
