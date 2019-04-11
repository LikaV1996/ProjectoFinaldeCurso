import { Component, OnInit, Inject, Input } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { PropagationEnvironment } from '../model/project';
import { ProjectService } from '../project.service';

@Component({
  selector: 'app-pedetails-dialog',
  templateUrl: './pedetails-dialog.component.html',
  styleUrls: ['./pedetails-dialog.component.css']
})
export class PEDetailsDialogComponent implements OnInit {
  @Input() pe: PropagationEnvironment;

  constructor(private projectService: ProjectService,
      public dialogRef: MatDialogRef<PEDetailsDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: PropagationEnvironment) {
  }

  public _pe: PropagationEnvironment = new PropagationEnvironment();
  currentTab = 0;

  ngOnInit() {
    this.pe = this.data[0];
    this.getDetails();
  }

  getTitle() {
    return this.pe.id > 0 ? 'Propagation environment properties' : 'New Propagation environment';
  }

  getDetails() {
    this._pe.name = this.pe.name;
    this._pe.resolution = this.pe.resolution;
  }

  setDetails() {
    this.pe.name = this._pe.name;
    this.pe.resolution = this._pe.resolution;
  }

  savePE() {
    this.setDetails();
    this.dialogRef.close('OK');
  }

  cancel() {
    this.dialogRef.close();
  }

}
