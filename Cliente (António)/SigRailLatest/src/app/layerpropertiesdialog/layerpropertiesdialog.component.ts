import { Project, BaseLayer, Railway, RailwayInfrastructure,
  Clutter, ClutterClass, RailwayElement, Building, BuildingGroup } from '../model/project';
import { ProjectService } from '../project.service';
import { Component, OnInit, OnChanges, Input, Inject, SimpleChanges } from '@angular/core';
import { MatSnackBar, MatSlider, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-layerpropertiesdialog',
  templateUrl: './layerpropertiesdialog.component.html',
  styleUrls: ['./layerpropertiesdialog.component.css']
})
export class LayerpropertiesdialogComponent  implements OnInit, OnChanges {
//  @Input() project: Project;
  @Input() layer: BaseLayer;
  @Input() railways: Railway[];
  @Input() elements: RailwayElement[];
  @Input() buildings: Building[];
  railway: Railway;
  infrastructure: RailwayInfrastructure;
  clutter: Clutter;
  buildingGroup: BuildingGroup;

  constructor(private projectService: ProjectService,
      private snackBar: MatSnackBar,
      public dialogRef: MatDialogRef<LayerpropertiesdialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {
  }

  public selectedIndex = 0;
  public _layer: BaseLayer = new BaseLayer();

  private _clutterClasses: ClutterClass[];
  public classColors: string[];

  ngOnInit() {
//    this.project = this.data[0];
    this.layer = this.data[1];
    if (this.isRailway) { this.railway = <Railway>this.layer; }
    if (this.isInfrastructure) {
      this.infrastructure = <RailwayInfrastructure>this.layer;
      this.railways = this.data[2];
      this.elements = this.data[3];
    }
    if (this.isClutter) {
      this.clutter = <Clutter>this.layer;
    }
    if (this.isBuildingGroup) {
      this.buildingGroup = <BuildingGroup>this.layer;
      this.buildings = this.data[2];
    }
    this.getDetails();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.getDetails();
  }

  isRailway() {
    return 'railwayPoints' in this.layer;
  }

  isInfrastructure() {
    return 'railwayElementsHref' in this.layer;
  }

  isClutter() {
    return 'clutterTilesHref' in this.layer;
  }

  isBuildingGroup() {
    return 'buildingsHref' in this.layer;
  }

  colorChange(c) {
    this._layer.properties.color = c;
  }

  getTitle() {
    return this.layer.id > 0 ? 'Layer properties' : 'New layer';
  }

  getDetails() {
    this._layer.name = this.layer.name;
    this._layer.coordinateSystem = this.layer.coordinateSystem;
    this._layer.properties = { ...this.layer.properties };
    if (this.isClutter()) {
      this._clutterClasses = [];
      this.classColors = [];
      for (const c of this.clutter.classes) {
        this._clutterClasses.push({
          color: {
            red: c.color.red,
            green: c.color.green,
            blue: c.color.blue,
            alpha: c.color.alpha
          },
          description: c.description,
          pixelValue: c.pixelValue
        });
        this.classColors.push('rgb(' + c.color.red + ',' + c.color.green + ',' + c.color.blue + ',' + c.color.alpha / 255 + ')');
      }
    }
  }

  setDetails() {
    this.layer.name = this._layer.name;
    this.layer.coordinateSystem = this._layer.coordinateSystem;
    this.layer.properties = { ...this._layer.properties };
    if (this.isClutter()) {
      this.clutter.classes = [];
      for (const c of this._clutterClasses) {
        this.clutter.classes.push({
          color: {
            red: c.color.red,
            green: c.color.green,
            blue: c.color.blue,
            alpha: c.color.alpha
          },
          description: c.description,
          pixelValue: c.pixelValue
        });
      }
    }
  }

  classColorChange(idx, value) {
    // const values = value.match(/^rgba\s*\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)$/i);
    const values = value.match(/^rgba?\((\d+),\s*(\d+),\s*(\d+)(?:,\s*(\d+(?:\.\d+)?))?\)$/);
    if (values) {
      this._clutterClasses[idx].color = {
        red: parseInt(values[1], 10),
        green: parseInt(values[2], 10),
        blue: parseInt(values[3], 10),
        alpha: !values[4] ? 255 : Math.round(parseFloat(values[4]) * 255)
      };
    } else {
      alert('Invalid color value: ' + value);
    }
  }

  formatSliderLabel(value: number | null) {
    if (!value) {
      return 0;
    }
    return Math.round(value * 100) + '%';
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
