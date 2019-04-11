import { RailwayInfrastructure, RailwayElement } from '../model/project';
import { ProjectService } from '../project.service';
import { Component, OnInit, Input, OnChanges, Inject, SimpleChanges } from '@angular/core';
import { MatSnackBar, MatRadioButton, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { inspect } from 'util';

@Component({
  selector: 'app-elementpropertiesdialog',
  templateUrl: './elementpropertiesdialog.component.html',
  styleUrls: ['./elementpropertiesdialog.component.css']
})
export class ElementpropertiesdialogComponent implements OnInit, OnChanges {
  @Input() infrastructure: RailwayInfrastructure;
  @Input() element: RailwayElement;

  constructor(public projectService: ProjectService,
      private snackBar: MatSnackBar,
      public dialogRef: MatDialogRef<ElementpropertiesdialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {
  }

  public _element: RailwayElement = new RailwayElement();

  ngOnInit() {
    this.infrastructure = this.data[0];
    this.element = this.data[1];
    this.getDetails();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.getDetails();
  }

  getTitle() {
    return this.element.id > 0 ? 'Edit Element' : 'Create Element';
  }

  getDetails() {
    this._element.name = this.element.name;
    this._element.class = this.element.class;
    this._element.properties = { ...this.element.properties };
    this._element.location = JSON.parse(JSON.stringify(this.element.location));
  }

  setDetails() {
    this.element.name = this._element.name;
    this.element.class = this._element.class;
    this.element.properties = { ...this._element.properties };
    this.element.location = JSON.parse(JSON.stringify(this._element.location));
  }

  saveElement() {
    this.setDetails();
    this.dialogRef.close('OK');
  }

  cancel() {
    this.dialogRef.close();
  }
}
