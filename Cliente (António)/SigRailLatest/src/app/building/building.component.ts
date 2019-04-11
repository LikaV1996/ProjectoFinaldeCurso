import { Building, BuildingGroup, Project } from '../model/project';
import { Component, OnInit, OnDestroy, OnChanges, DoCheck, SimpleChanges, Input, NgZone } from '@angular/core';

import {
  Map as LeafletMap, Polygon, Polyline, DivIcon, LatLng, Marker, FeatureGroup
} from 'leaflet';
import { ContextMenuComponent, ContextMenuService } from 'ngx-contextmenu';
import { ProjectService } from '../project.service';
import { Feature, Point } from 'geojson';
import { MatSnackBar } from '@angular/material';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-building',
  templateUrl: './building.component.html',
  styleUrls: ['./building.component.css']
})
export class BuildingComponent implements OnInit, OnDestroy, OnChanges, DoCheck {
  @Input() map: LeafletMap;
  @Input() project: Project;
  @Input() buildingGroup: BuildingGroup;
  @Input() building: Building;
  @Input() layerGroup: FeatureGroup;
  @Input() buildingMenu: ContextMenuComponent;
  @Input() layerMenu: ContextMenuComponent;
  @Input() pointMenu: ContextMenuComponent;
  @Input() groupEditMode: boolean;
  @Input() editMode: boolean;
  @Input() visible: boolean;
  @Input() color: string;

  private polygon: Polygon;
  private polyStyle: any;

  selectedPoint: number = null;

  // Point selection "square"
  pointSelectIcon = new DivIcon({className: 'point-select-icon', iconSize: [16, 16]});
  pointSelectLayer = new Marker(
      new LatLng(0, 0), {icon: this.pointSelectIcon, interactive: false}
  );

  // Point insert icons
  pointInsertIcon = new DivIcon({className: 'insert-point-icon'});
  pointInsertLayer = new Marker(new LatLng(0, 0), {icon: this.pointInsertIcon});
  pointInsertLayer2 = new Marker(new LatLng(0, 0), {icon: this.pointInsertIcon});

  bulildingAddPoint: number[] = null; // building point after which to add
  buildingAddPointPoly: Polyline = new Polyline([], {
    weight: 3,
    opacity: .8,
    color: '#459fa5',
    dashArray: '5, 5',
    interactive: false,
  });
  buildingNewPoint: Feature<Point>;

  private editLayerGroup: FeatureGroup = new FeatureGroup();


  constructor(private projectService: ProjectService,
    private contextMenuService: ContextMenuService, private zone: NgZone) { }

  ngOnInit() {
    this.getPolyStyle();
    this.polygon = new Polygon([], this.polyStyle);
    this.createPolygon();
    this.layerGroup.addTo(this.map);
    this.pointInsertLayer.addEventListener('click', () => {
      this.insertPointAfter();
    });
    this.pointInsertLayer2.addEventListener('click', () => {
      this.insertPointBefore();
    });
    if (this.building.properties && this.building.properties.addPointMode) {
      this.projectService.pointAddCursor = true;
      this.map.on('mousemove', this.onMapMouseMove, this);
      this.map.on('click', this.onMapClick, this);
      this.map.on('keypress', this.onKey, this);
      this.buildingAddPointPoly.addTo(this.map);
    }
    if (this.building.properties && this.building.properties.editMode) {
      this.setEditMode(true);
    }
  }

  ngDoCheck(): void {
    if (this.building.properties && this.building.properties.pointsChanged) {
      this.updatePolygon();
      this.updatePointSelectLayer();
      this.setEditMode(this.building.properties.editMode);
      delete this.building.properties.pointsChanged;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.editMode && !changes.editMode.isFirstChange()) {
      this.setEditMode(this.editMode);
    }
    if (changes.visible && !changes.visible.isFirstChange()) {
      if (this.visible) {
        this.layerGroup.addTo(this.map);
        this.editLayerGroup.addTo(this.map);
      } else {
        this.layerGroup.removeFrom(this.map);
        this.editLayerGroup.removeFrom(this.map);
      }
    }
    if ((changes.color && !changes.color.isFirstChange()) || (changes.elements && !changes.elements.isFirstChange())) {
      this.getPolyStyle();
      this.polygon.setStyle(this.polyStyle);
    }
  }

  ngOnDestroy(): void {
    this.editLayerGroup.removeFrom(this.map);
    this.polygon.removeFrom(this.map);
    this.pointSelectLayer.removeFrom(this.map);
    this.pointInsertLayer.removeFrom(this.map);
    this.pointInsertLayer2.removeFrom(this.map);
    this.buildingAddPointPoly.removeFrom(this.map);
    this.map.off('mousemove', this.onMapMouseMove, this);
    this.map.off('click', this.onMapClick, this);
    this.map.off('keypress', this.onKey, this);
  }

  createPolygon() {
    // cria a Polygon
    this.getPolyStyle();
    const pointsLatLng = [];
    for (const p of this.building.perimeter.coordinates[0]) {
      pointsLatLng.push({lat: p[1], lng: p[0]});
    }
    this.polygon = new Polygon(pointsLatLng, this.polyStyle);
    const _comp = this;
    const b = this.building;
    this.polygon.on('contextmenu', (ev) => {
      _comp.zone.run( () => {
        ev.originalEvent.preventDefault();
        ev.originalEvent.stopPropagation();
        _comp.contextMenuService.show.next({
          contextMenu: (_comp.groupEditMode ? _comp.buildingMenu : _comp.layerMenu),
          event: ev.originalEvent,
          item: (_comp.groupEditMode ? [_comp.buildingGroup, b] : _comp.buildingGroup),
        });
      });
    });
    this.polygon.bindTooltip(this.buildingGroup.name + '<br />' + this.building.name);
    this.polygon.addTo(this.layerGroup);
  }

  updatePolygon() {
    // actualiza o polygon
    this.getPolyStyle();
    this.polygon.setStyle(this.polyStyle);
    const pointsLatLng = [];
    for (const p of this.building.perimeter.coordinates[0]) {
      pointsLatLng.push({lat: p[1], lng: p[0]});
    }
    this.polygon.setLatLngs(pointsLatLng);
  }

  getPolyStyle() {
    const properties = {
      visible: true,
      weight: 3,
      color: 'blue',
      fillColor: 'blue',
      fillOpacity: 0.5,
    };
    this.polyStyle = {
      weight: properties.weight,
      color: this.buildingGroup.properties.color,
      fillColor: this.buildingGroup.properties.fillColor,
      fillOpacity: properties.fillOpacity,
    };
  }

  setEditMode(on: boolean) {
    // if (this.buildingEditLayer) { this.railwayEditLayer.removeFrom(this.map); }
    this.editLayerGroup.removeFrom(this.map);
    this.editLayerGroup.clearLayers();
    if (on) {
      const pointIcon = new DivIcon({className: 'railway-edit-icon'});
      const _comp = this;
      this.building.perimeter.coordinates[0].forEach((p, idx) => {
        const ei = new Marker(new LatLng(p[1], p[0]), {icon: pointIcon, draggable: true});
        ei.on('contextmenu',  (ev) => {
            _comp.zone.run( () => {
              ev.originalEvent.preventDefault();
              ev.originalEvent.stopPropagation();
              _comp.contextMenuService.show.next({
                contextMenu: _comp.pointMenu,
                event: ev.originalEvent,
                item: [_comp.building, idx],
              });
            });
        });
        ei.addTo(this.editLayerGroup);
        ei.addEventListener('drag', function(ev) {
          p[1] = ev.target.getLatLng().lat;
          p[0] = ev.target.getLatLng().lng;
          _comp.updatePolygon();
          _comp.updatePointSelectLayer();
        });
        ei.addEventListener('dragend', function() {
          if (_comp.building.perimeter.coordinates[0].length > 2) {
            _comp.saveBuilding();
          }
        });
        ei.addEventListener('dragstart', function() {
            _comp.selectPoint(idx);
          });
          ei.addEventListener('click', function() {
            if (_comp.building.properties.addPointMode) { _comp.cancelAddPointMode(); }
            _comp.selectPoint(idx);
          });
      });
      this.editLayerGroup.addTo(this.map);
    } else { // off
      // this.editLayerGroup.removeFrom(this.map);
      // this.editLayerGroup.clearLayers();
      this.selectedPoint = null;
      this.updatePointSelectLayer();
      this.cancelAddPointMode();
    }
  }

  cancelAddPointMode() {
    this.projectService.pointAddCursor = false;
    this.map.off('mousemove', this.onMapMouseMove, this);
    this.map.off('click', this.onMapClick, this);
    this.map.off('keypress', this.onKey, this);
    this.buildingAddPointPoly.removeFrom(this.map);
    delete this.building.properties.addPointMode;
  }

  selectPoint(idx: number) {
    this.selectedPoint = idx;
    this.updatePointSelectLayer();
  }

  insertPointBefore() {
    const newPoint = [this.pointInsertLayer2.getLatLng().lng, this.pointInsertLayer2.getLatLng().lat];
    if (this.selectedPoint === 0) {
      this.building.perimeter.coordinates[0].unshift(newPoint);
    } else {
      this.building.perimeter.coordinates[0].splice(this.selectedPoint, 0, newPoint);
    }
    this.updatePointSelectLayer();
    this.updatePolygon();
    this.setEditMode(true);
  }

  insertPointAfter() {
    const newPoint = [this.pointInsertLayer.getLatLng().lng, this.pointInsertLayer.getLatLng().lat];
    if (this.selectedPoint === this.building.perimeter.coordinates[0].length - 1) {
      this.building.perimeter.coordinates[0].push(newPoint);
    } else {
      this.building.perimeter.coordinates[0].splice(this.selectedPoint + 1, 0, newPoint);
    }
    this.selectedPoint += 1;
    this.updatePointSelectLayer();
    this.updatePolygon();
    this.setEditMode(true);
  }

  saveBuilding() {
    if (this.building.id === 0) {
      this.projectService.addProjectBuildingGroupBuilding(this.project, this.buildingGroup, this.building).subscribe(resp => {
        this.projectService.getProjectBuildingGroupBuilding(resp.headers.get('Location')).subscribe(b => this.building = b);
      });
    } else {
      this.projectService.updateProjectBuildingGroupBuilding(this.project, this.buildingGroup, this.building).subscribe();
    }
  }

  onMapMouseMove(e) {
    const np = this.building.perimeter.coordinates[0].length;
    if (this.building.properties.addPointMode && np > 0) {
      // if (this.selectedPoint != null) {
      if (np === 1) {
        this.buildingAddPointPoly.setLatLngs([
         [e.latlng.lat, e.latlng.lng],
         [this.building.perimeter.coordinates[0][0][1], this.building.perimeter.coordinates[0][0][0]]
        ]);
      } else {
        this.buildingAddPointPoly.setLatLngs([
          [this.building.perimeter.coordinates[0][np - 1][1], this.building.perimeter.coordinates[0][np - 1][0]],
         [e.latlng.lat, e.latlng.lng],
         [this.building.perimeter.coordinates[0][0][1], this.building.perimeter.coordinates[0][0][0]]
        ]);
      }
      // this.buildingNewPoint.geometry.coordinates[0] = e.latlng.lng;
      // this.buildingNewPoint.geometry.coordinates[1] = e.latlng.lat;
    }
  }

  updatePointSelectLayer() {
    this.pointInsertLayer.removeFrom(this.map);
    this.pointInsertLayer2.removeFrom(this.map);
    if (this.selectedPoint == null) {
      this.pointSelectLayer.removeFrom(this.map);
      this.pointInsertLayer.removeFrom(this.map);
      this.pointInsertLayer2.removeFrom(this.map);
    } else {
      this.pointSelectLayer.addTo(this.map);
      this.pointSelectLayer.setLatLng(new LatLng(
        this.building.perimeter.coordinates[0][this.selectedPoint][1],
        this.building.perimeter.coordinates[0][this.selectedPoint][0])
      );
      // create add point markers
      // const pointIcon = new DivIcon({className: 'railway-edit-icon'});
      // const pIdx = this.railway.railwayPoints.features.indexOf(this.railwayPoint); // this.railwayPoint.properties.idx;
      const x1 = this.building.perimeter.coordinates[0][this.selectedPoint][0];
      const y1 = this.building.perimeter.coordinates[0][this.selectedPoint][1];
      const np = this.building.perimeter.coordinates[0].length;
      const nextIdx = (this.selectedPoint + 1) % np;
      let x2 = this.building.perimeter.coordinates[0][nextIdx][0];
      let xDelta = x2 - x1;
      let y2 = this.building.perimeter.coordinates[0][nextIdx][1];
      let yDelta = y2 - y1;
      this.pointInsertLayer.setLatLng(new LatLng(y1 + yDelta / 2, x1 + xDelta / 2));
      this.pointInsertLayer.addTo(this.map);
      let prevIdx = this.selectedPoint - 1;
      if (prevIdx < 0) { prevIdx = np - 1; }
      x2 = this.building.perimeter.coordinates[0][prevIdx][0];
      xDelta = x2 - x1;
      y2 = x2 = this.building.perimeter.coordinates[0][prevIdx][1];
      yDelta = y2 - y1;
      this.pointInsertLayer2.setLatLng(new LatLng(y1 + yDelta / 2, x1 + xDelta / 2));
      this.pointInsertLayer2.addTo(this.map);
    }
  }

  onMapClick(e) {
    const np = this.building.perimeter.coordinates[0].length;
    if (np < 30) {
      console.log('np: ' + np);
       this.building.perimeter.coordinates[0].push([e.latlng.lng, e.latlng.lat]);
       if (np < 2) {
         this.createPolygon();
         this.bulildingAddPoint = [e.latlng.lng, e.latlng.lat];
       } else {
         this.saveBuilding();
       }
       this.setEditMode(true);
       this.updatePolygon();
    } else {
      // if (this.railwayNewPoint != null) {
      //   if (this.railwayAddTo > 0) { // add point to end
      //     this.railway.railwayPoints.features.push(this.railwayNewPoint);
      //     this.editableFeatures.features.push(this.railwayNewPoint);
      //     this.addPointToEnd();
      //     this.railway.properties.pointsChanged = true;
      //     if (this.railway.railwayPoints.features.length >= 2) {
      //       this.railway.properties.addPoint = true;
      //       this.saveEvent.emit(this.railway);
      //     }
      //   } else {
      //     this.railway.railwayPoints.features.unshift(this.railwayNewPoint);
      //     this.editableFeatures.features.unshift(this.railwayNewPoint);
      //     this.addPointToBeginning();
      //     this.railway.properties.pointsChanged = true;
      //     if (this.railway.railwayPoints.features.length >= 2) { this.saveEvent.emit(this.railway); }
      //   }
      //   this.updatePointSelectLayer();
      //   this.updateRailwayEditLayer();
      // }
    }
  }

  onKey(event) {
    switch (event.originalEvent.code) {
      case 'KeyD': {
        // this.delPoint(this.railwayPoint);
        break;
      }
      case 'ArrowLeft': {
        alert('left');
        const np = this.building.perimeter.coordinates[0].length;
        if (this.selectedPoint === 0) {
          this.selectedPoint = np - 1;
        } else {this.selectedPoint = (this.selectedPoint + 1) % np; }
        this.updatePointSelectLayer();
        break;
      }
      case 'ArrowRight': {
        // if (this.railway.railwayPoints.features.indexOf(this.railwayPoint) < this.railway.railwayPoints.features.length - 1) {
        //   this.selectPoint(this.railway.railwayPoints.features[this.railway.railwayPoints.features.indexOf(this.railwayPoint) + 1]);
        // }
        event.stopPropagation();
      }
    }
  }
}
