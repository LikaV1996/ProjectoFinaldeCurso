import { BuildingGroup, Building } from '../model/project';
import { ProjectService } from '../project.service';
import { Component, OnInit, OnChanges, Input, Inject, SimpleChanges } from '@angular/core';
import { MatSnackBar, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-buildingpropertiesdialog',
  templateUrl: './buildingpropertiesdialog.component.html',
  styleUrls: ['./buildingpropertiesdialog.component.css']
})
export class BuildingpropertiesdialogComponent  implements OnInit, OnChanges {
  @Input() buildingGroup: BuildingGroup;
  @Input() building: Building;

  constructor(public projectService: ProjectService,
      private snackBar: MatSnackBar,
      public dialogRef: MatDialogRef<BuildingpropertiesdialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {
  }

  public _building: Building = new Building();

  ngOnInit() {
    this.buildingGroup = this.data[0];
    this.building = this.data[1];
    this.getDetails();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.getDetails();
  }

  getTitle() {
    return this.building.id > 0 ? 'Edit Building' : 'Create Building';
  }

  getDetails() {
    this._building.name = this.building.name;
    this._building.elevation = this.building.elevation;
    this._building.perimeter = JSON.parse(JSON.stringify(this.building.perimeter));
  }

  setDetails() {
    this.building.name = this._building.name;
    this.building.elevation = this._building.elevation;
    this.building.perimeter = JSON.parse(JSON.stringify(this._building.perimeter));
  }

  saveBuilding() {
    this.setDetails();
    this.dialogRef.close('OK');
  }

  cancel() {
    this.dialogRef.close();
  }
}
