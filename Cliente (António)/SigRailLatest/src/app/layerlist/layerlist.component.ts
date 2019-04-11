import { BaseLayer, Railway } from '../model/project';
import { ProjectService } from '../project.service';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-layerlist',
  templateUrl: './layerlist.component.html',
  styleUrls: ['./layerlist.component.scss']
})
export class LayerlistComponent implements OnInit {
  @Output() layerSelect = new EventEmitter<BaseLayer>();
  @Output() layerOpen = new EventEmitter<BaseLayer>();
  layers: BaseLayer[] = [];
  selectedLayer: BaseLayer;
  layerType = 'railway';
  railways: Railway[];
  orderBy: string;
  sort: string;
  epp = 30;
  pageN = 1;

  status = 'Loading...';

  constructor(private projectService: ProjectService) { }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    const filter: any = {
      size: this.epp,
      'page': this.pageN
    };
    if (this.orderBy) {
      filter.orderBy = this.orderBy;
      if (this.sort == null) { this.sort = 'asc'; }
      filter.sort = this.sort;
    }
    switch (this.layerType) {
      case 'railway':
        this.projectService.filterAllRailways(filter).subscribe(
          railways  => {
            for (const railway of railways) {railway.railwayPoints = {type: 'FeatureCollection', features: []}; }
            this.layers = railways;
            this.status = railways.length > 0 ? ('Railways found: ' + railways.length) : 'No layers found.';
          },
          error => {
            this.status = 'Error getting published railways:' + error.statusText;
        });
        break;
      case 'clutter':
        this.projectService.filterAllClutters(filter).subscribe(
          clutters  => {
            for (const clutter of clutters) {clutter.clutterTilesHref = ''; }
            this.layers = clutters;
            this.status = clutters.length > 0 ? ('Clutters found: ' + clutters.length) : 'No layers found.';
          },
          error => {
            this.status = 'Error getting published clutters:' + error.statusText;
        });
        break;
      case 'raiwayInfrastructure':
        this.projectService.filterAllInfrastructures(filter).subscribe(
          infrastructures  => {
            this.layers = infrastructures;
            this.status = infrastructures.length > 0 ? ('Railway Infrastructures found: ' + infrastructures.length) : 'No layers found.';
          },
          error => {
            this.status = 'Error getting published infrastructures:' + error.statusText;
        });
      break;
      case 'buildingGroup':
        this.projectService.filterAllBuildingGroups(filter).subscribe(
          buildingGroups  => {
            for (const bg of buildingGroups) {bg.buildingsHref = ''; }
            this.layers = buildingGroups;
            this.status = buildingGroups.length > 0 ? ('Building groups found: ' + buildingGroups.length) : 'No layers found.';
          },
          error => {
            this.status = 'Error getting published buildingGroups:' + error.statusText;
        });
      break;
    }
  }

  selectLayer(layer: BaseLayer) {
    this.selectedLayer = layer;
    this.layerSelect.emit(layer);
  }

  openLayer(layer: BaseLayer) {
    this.layerOpen.emit(layer);
  }
}
