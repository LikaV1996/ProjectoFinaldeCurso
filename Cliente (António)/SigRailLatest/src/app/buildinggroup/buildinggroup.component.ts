import { BuildingGroup, Building, Project } from '../model/project';
import { Component, Input, Output, EventEmitter } from '@angular/core';

import {
  Map as LeafletMap, FeatureGroup
} from 'leaflet';
import { ContextMenuComponent } from 'ngx-contextmenu';
import { Deflate } from '../model/mapUtils';

@Component({
  selector: 'app-buildinggroup',
  templateUrl: './buildinggroup.component.html',
  styleUrls: ['./buildinggroup.component.css']
})
export class BuildinggroupComponent {
  @Input() map: LeafletMap;
  @Input() project: Project;
  @Input() buildingGroup: BuildingGroup;
  @Input() buildings: Building[];
  @Input() layerGroup: Deflate; // FeatureGroup;
  @Input() layerMenu: ContextMenuComponent;
  @Input() buildingMenu: ContextMenuComponent;
  @Input() pointMenu: ContextMenuComponent;
  @Input() editMode: boolean;
  @Output() cancelEvent: EventEmitter<BuildingGroup> = new EventEmitter<BuildingGroup>();

  constructor() {
  }

  newBuilding(ev) {
    ev.preventDefault();
    ev.stopPropagation();
    const newBuilding: Building = {
      elevation: 0,
      id: 0,
      name: 'new building',
      properties: { editMode: true, addPointMode: true },
      perimeter: {
        type: 'Poligon',
        coordinates: [new Array<Array<number>>()]
      }
    };
    this.buildings.push(newBuilding);
  }

  cancel() {
    this.cancelEvent.emit(this.buildingGroup);
  }

}
