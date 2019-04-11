import { Railway, Project } from '../model/project';
import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, NgZone, DoCheck } from '@angular/core';
import {
  Map as LeafletMap, Layer, DivIcon, geoJSON, Marker, LatLng, Polyline, LayerGroup,
} from 'leaflet';
import { Feature, Point, FeatureCollection } from 'geojson';
import { ContextMenuComponent, ContextMenuService } from 'ngx-contextmenu';
import { ProjectService } from '../project.service';

const MAXPOINTS = 250;

@Component({
  selector: 'app-railwayedit',
  templateUrl: './railwayedit.component.html',
  styleUrls: ['./railwayedit.component.css']
})
export class RailwayeditComponent implements OnInit, OnDestroy, DoCheck {
  @Input() map: LeafletMap;
  @Input() project: Project;
  @Input() railway: Railway;
  @Input() pointMenu: ContextMenuComponent;
  @Output() changeEvent: EventEmitter<Railway> = new EventEmitter<Railway>();
  @Output() saveEvent: EventEmitter<Railway> = new EventEmitter<Railway>();
  @Output() cancelEvent: EventEmitter<Railway> = new EventEmitter<Railway>();

  railwayPoint: Feature<Point> = null; // Selected Point

  editableFeatures: FeatureCollection<Point> = {'type': 'FeatureCollection', 'features': []}; // points beeing edited
  railwayEditLayer = new Layer(); // edit markers

  // startIndex: number;
  // endIndex: number;

  // Point selection "square"
  pointSelectIcon = new DivIcon({className: 'point-select-icon', iconSize: [16, 16]});
  pointSelectLayer = new Marker(
      new LatLng(0, 0), {icon: this.pointSelectIcon, interactive: false}
  );
  // Point insert icons
  pointInsertIcon = new DivIcon({className: 'insert-point-icon'});
  pointInsertLayer = new Marker(new LatLng(0, 0), {icon: this.pointInsertIcon});
  pointInsertLayer2 = new Marker(new LatLng(0, 0), {icon: this.pointInsertIcon});

  railwayAddPoint: Feature<Point> = null; // railway point to add to
  railwayAddTo = 0; // 0 = beginning; >0 = end
  railwayAddPointPoly: Polyline = new Polyline([], {
    weight: 3,
    opacity: .8,
    color: '#459fa5',
    dashArray: '5, 5',
    interactive: false,
  });
  railwayNewPoint: Feature<Point>;


  constructor(private projectService: ProjectService, private contextMenuService: ContextMenuService,
    private zone: NgZone) {}

  ngOnInit() {
    console.log('railway edit init: ' + this.railway.name);
    this.pointInsertLayer.addEventListener('click', () => {
      this.insertPointAfter();
      this.updateRailwayEditLayer();
    });
    this.pointInsertLayer2.addEventListener('click', () => {
      this.insertPointBefore();
      this.updateRailwayEditLayer();
    });
    this.map.on('mousemove', this.onMapMouseMove, this);
    this.map.on('click', this.onMapClick, this);
    this.map.on('keypress', this.onKey, this);

    if (this.railway.railwayPoints.features.length === 0) {
      this.addPointToBeginning();
    }

    // choose editable points
    let inside = 0;
    let segmentStarted = false;
    let segmentEnded = false;
    const allIn = this.railway.railwayPoints.features.length <= MAXPOINTS;
    for (let i = 0; i < this.railway.railwayPoints.features.length && inside < MAXPOINTS && !segmentEnded; i++) {
      if (allIn || this.map.getBounds().contains(new LatLng(this.railway.railwayPoints.features[i].geometry.coordinates[1],
          this.railway.railwayPoints.features[i].geometry.coordinates[0]))) {
        if (this.editableFeatures.features.length === 0) {
//          this.startIndex = i;
          segmentStarted = true;
        }
        inside++;
        this.editableFeatures.features.push(this.railway.railwayPoints.features[i]);
 //       this.endIndex = i;
      } else {
        if (segmentStarted) {
          segmentEnded = true;
        }
      }
    }
    console.log('editable points:' + this.editableFeatures.features.length);
    this.updateRailwayEditLayer();
    this.updatePointSelectLayer();
  }

  ngOnDestroy() {
    // this.revertRailway();
    this.railway.properties.pointsChanged = true;
    this.railwayEditLayer.removeFrom(this.map);
    this.pointSelectLayer.removeFrom(this.map);
    this.pointInsertLayer.removeFrom(this.map);
    this.pointInsertLayer2.removeFrom(this.map);
    this.railwayAddPointPoly.removeFrom(this.map);
    this.map.off('mousemove', this.onMapMouseMove, this);
    this.map.off('click', this.onMapClick, this);
    this.map.off('keypress', this.onKey, this);
  }

  ngDoCheck() {
    if (this.railway.properties.pointsEditChanged) {
      this.updateRailwayEditLayer();
      this.updatePointSelectLayer();
      delete this.railway.properties.pointsEditChanged;
    }
    if (this.railway.properties.addPoint) {
      this.addPointToEnd();
      delete this.railway.properties.addPoint;
    }
  }

  updateRailwayEditLayer() {
    if (this.railwayEditLayer) { this.railwayEditLayer.removeFrom(this.map); }
    const pointIcon = new DivIcon({className: 'railway-edit-icon'});
    const _comp = this;

    this.railwayEditLayer = geoJSON(this.editableFeatures/*railway.railwayPoints*/, {
      pointToLayer: function(feature, latlng) {
        const ei = new Marker(latlng, {icon: pointIcon, draggable: true});
        ei.on('contextmenu',  (ev) => {
          _comp.zone.run( () => {
            ev.originalEvent.preventDefault();
            ev.originalEvent.stopPropagation();
            _comp.contextMenuService.show.next({
              contextMenu: _comp.pointMenu,
              event: ev.originalEvent,
              item: [_comp.railway, feature],
            });
          });
        });
        ei.addEventListener('drag', function(ev) {
          feature.geometry.coordinates[0] = ev.target.getLatLng().lng;
          feature.geometry.coordinates[1] = ev.target.getLatLng().lat;
          _comp.updatePointSelectLayer();
          _comp.railway.properties.pointsChanged = true;
        });
        ei.addEventListener('dragstart', function() {
          _comp.selectPoint(feature);
        });
        ei.addEventListener('dragend', function() {
          _comp.saveEvent.emit(_comp.railway);
        });
        ei.addEventListener('click', function() {
          _comp.selectPoint(feature);
        });
        return ei;
      }
    });
    this.railwayEditLayer.addTo(this.map);
    this.updatePointSelectLayer();
  }

  selectPoint(feature: Feature<Point>) {
    this.railwayPoint = feature;
    this.updatePointSelectLayer();
    this.addPointCancel();
  }

  updatePointSelectLayer() {
    this.pointInsertLayer.removeFrom(this.map);
    this.pointInsertLayer2.removeFrom(this.map);
    if (this.railwayPoint == null) {
      this.pointSelectLayer.removeFrom(this.map);
    } else {
      this.pointSelectLayer.addTo(this.map);
      this.pointSelectLayer.setLatLng(new LatLng(
        this.railwayPoint.geometry.coordinates[1],
        this.railwayPoint.geometry.coordinates[0])
      );
      // create add point markers
      // const pointIcon = new DivIcon({className: 'railway-edit-icon'});
      const pIdx = this.railway.railwayPoints.features.indexOf(this.railwayPoint); // this.railwayPoint.properties.idx;
      const x1 = this.railwayPoint.geometry.coordinates[0];
      const y1 = this.railwayPoint.geometry.coordinates[1];
      if (pIdx < this.railway.railwayPoints.features.length - 1) {
        const x2 = this.railway.railwayPoints.features[pIdx + 1].geometry.coordinates[0];
        const xDelta = x2 - x1;
        const y2 = this.railway.railwayPoints.features[pIdx + 1].geometry.coordinates[1];
        const yDelta = y2 - y1;
        this.pointInsertLayer.setLatLng(new LatLng(y1 + yDelta / 2, x1 + xDelta / 2));
        this.pointInsertLayer.addTo(this.map);
      }
      if (pIdx > 0) {
        const x2 = this.railway.railwayPoints.features[pIdx - 1].geometry.coordinates[0];
        const xDelta = x2 - x1;
        const y2 = this.railway.railwayPoints.features[pIdx - 1].geometry.coordinates[1];
        const yDelta = y2 - y1;
        this.pointInsertLayer2.setLatLng(new LatLng(y1 + yDelta / 2, x1 + xDelta / 2));
        this.pointInsertLayer2.addTo(this.map);
      }
    }
  }

  insertPointBefore() {
    this.addPointCancel();
    const pIdx = this.railway.railwayPoints.features.indexOf(this.railwayPoint); // this.railwayPoint.properties.idx;
    const x1 = this.railwayPoint.geometry.coordinates[0];
    const y1 = this.railwayPoint.geometry.coordinates[1];
    const z1 = this.railwayPoint.geometry.coordinates[2];
    if (pIdx > 0) {
      const x2 = this.railway.railwayPoints.features[pIdx - 1].geometry.coordinates[0];
      const xDelta = x2 - x1;
      const y2 = this.railway.railwayPoints.features[pIdx - 1].geometry.coordinates[1];
      const yDelta = y2 - y1;
      const z2 = this.railway.railwayPoints.features[pIdx - 1].geometry.coordinates[1];
      const zDelta = z2 - z1;
      this.pointInsertLayer.setLatLng(new LatLng(y1 + yDelta / 2, x1 + xDelta / 2));
      this.pointInsertLayer.addTo(this.map);
      const kp1 = this.railwayPoint.properties.kilometricPoint;
      const kp2 = this.railway.railwayPoints.features[pIdx - 1].properties.kilometricPoint;
      const kpDelta = kp2 - kp1;
      const newPoint: Feature<Point> = {
        'type': 'Feature',
        geometry: {
          'type': 'Point',
          coordinates: [x1 + xDelta / 2, y1 + yDelta / 2, z1 + zDelta / 2]
        },
        properties: {kilometricPoint: kp1 + kpDelta / 2}
      };
      this.railway.railwayPoints.features.splice(pIdx, 0, newPoint);
      this.railwayPoint = newPoint;
      this.editableFeatures.features.push(newPoint);
      this.updateRailwayEditLayer();
    }
  }

  insertPointAfter() {
    this.addPointCancel();
    const pIdx = this.railway.railwayPoints.features.indexOf(this.railwayPoint); // this.railwayPoint.properties.idx;
    const x1 = this.railwayPoint.geometry.coordinates[0];
    const y1 = this.railwayPoint.geometry.coordinates[1];
    const z1 = this.railwayPoint.geometry.coordinates[2];
    if (pIdx < this.railway.railwayPoints.features.length - 1) {
      const x2 = this.railway.railwayPoints.features[pIdx + 1].geometry.coordinates[0];
      const xDelta = x2 - x1;
      const y2 = this.railway.railwayPoints.features[pIdx + 1].geometry.coordinates[1];
      const yDelta = y2 - y1;
      const z2 = this.railway.railwayPoints.features[pIdx + 1].geometry.coordinates[1];
      const zDelta = z2 - z1;
      this.pointInsertLayer.setLatLng(new LatLng(y1 + yDelta / 2, x1 + xDelta / 2));
      this.pointInsertLayer.addTo(this.map);
      const kp1 = this.railwayPoint.properties.kilometricPoint;
      const kp2 = this.railway.railwayPoints.features[pIdx + 1].properties.kilometricPoint;
      const kpDelta = kp2 - kp1;
      const newPoint: Feature<Point> = {
        'type': 'Feature',
        geometry: {
          'type': 'Point',
          coordinates: [x1 + xDelta / 2, y1 + yDelta / 2, z1 + zDelta / 2]
        },
        properties: {kilometricPoint: kp1 + kpDelta / 2}
      };
      this.railway.railwayPoints.features.splice(pIdx + 1, 0, newPoint);
      this.railwayPoint = newPoint;
      this.editableFeatures.features.push(newPoint);
      this.updateRailwayEditLayer();
    }
  }

  addPointToEnd(ev?) {
    if (ev) { ev.stopPropagation(); }
    this.railwayAddPointPoly.addTo(this.map);
    this.railwayAddPoint = this.railway.railwayPoints.features[this.railway.railwayPoints.features.length - 1];
    this.railwayPoint = this.railwayAddPoint;
    this.railwayNewPoint = {
        'geometry': {
          'coordinates': [this.railwayAddPoint.geometry.coordinates[0], this.railwayAddPoint.geometry.coordinates[1],
          this.railwayAddPoint.geometry.coordinates[2]],
          'type': 'Point'
        },
        'properties': {
          'kilometricPoint': 0
        },
        'type': 'Feature'
      };
    this.railwayAddTo = 1;
  }

  addPointToBeginning(ev?) {
    if (ev) { ev.stopPropagation(); }
    this.railwayNewPoint = {
        'geometry': {
          'coordinates': [0, 0, 0],
          'type': 'Point'
        },
        'properties': {
          'kilometricPoint': 0,
        },
        'type': 'Feature'
      };
    if (this.railway.railwayPoints.features.length > 0) {
      this.railwayAddPointPoly.addTo(this.map);
      this.railwayAddPoint = this.railway.railwayPoints.features[0];
      this.railwayPoint = this.railwayAddPoint;
      this.railwayNewPoint.geometry.coordinates = [ this.railwayAddPoint.geometry.coordinates[0],
        this.railwayAddPoint.geometry.coordinates[1], this.railwayAddPoint.geometry.coordinates[2]];
      this.railwayNewPoint.properties.kilometricPoint = this.railway.railwayPoints.features[0].properties.kilometricPoint / 2;
    }
    this.railwayAddTo = 0;
  }

  addPointCancel() {
    this.railwayAddPoint = null;
    this.railwayNewPoint = null;
    this.railwayAddPointPoly.setLatLngs([[0, 0], [0, 0]]);
    this.railwayAddPointPoly.removeFrom(this.map);
  }

  delPoint(p: Feature<Point>) {
    this.addPointCancel();
    this.railway.railwayPoints.features.splice(this.railway.railwayPoints.features.indexOf(p)/*p.properties.idx*/, 1);
    this.editableFeatures.features.splice(this.editableFeatures.features.indexOf(p), 1);
    this.saveEvent.emit(this.railway);
    this.railway.properties.pointsChanged = true;
    this.updateRailwayEditLayer();
    this.selectPoint(null);
    if (this.railway.railwayPoints.features.length === 0) { this.addPointToBeginning(); }
  }

  onMapMouseMove(e) {
    if (this.railwayNewPoint != null) {
      if (this.railwayPoint != null) {
        this.railwayAddPointPoly.setLatLngs([
        [this.railwayPoint.geometry.coordinates[1], this.railwayPoint.geometry.coordinates[0]],
        [e.latlng.lat, e.latlng.lng]]);
      }
      this.railwayNewPoint.geometry.coordinates[0] = e.latlng.lng;
      this.railwayNewPoint.geometry.coordinates[1] = e.latlng.lat;
    }
  }

  onMapClick(e) {
    console.log('mapClick');
    if (this.railway.railwayPoints.features.length === 0) {
      // alert('first point');
      // const newPoint:
      this.railway.railwayPoints.features.push( <Feature<Point>> {
        'type': 'Feature',
        geometry: {
          'type': 'Point',
          coordinates: [e.latlng.lng, e.latlng.lat, 0]
        },
        properties: {kilometricPoint: 0}
      });
      this.addPointToEnd();
    } else {
      if (this.railwayNewPoint != null) {
        if (this.railwayAddTo > 0) { // add point to end
          this.railway.railwayPoints.features.push(this.railwayNewPoint);
          this.editableFeatures.features.push(this.railwayNewPoint);
          this.addPointToEnd();
          this.railway.properties.pointsChanged = true;
          if (this.railway.railwayPoints.features.length >= 2) {
            this.railway.properties.addPoint = true;
            this.saveEvent.emit(this.railway);
          }
        } else {
          this.railway.railwayPoints.features.unshift(this.railwayNewPoint);
          this.editableFeatures.features.unshift(this.railwayNewPoint);
          this.addPointToBeginning();
          this.railway.properties.pointsChanged = true;
          if (this.railway.railwayPoints.features.length >= 2) { this.saveEvent.emit(this.railway); }
        }
        this.updatePointSelectLayer();
        this.updateRailwayEditLayer();
      }
    }
  }

  onKey(event) {
    switch (event.originalEvent.code) {
      case 'KeyD': {
        this.delPoint(this.railwayPoint);
        break;
      }
      case 'ArrowLeft': {
        if (this.railway.railwayPoints.features.indexOf(this.railwayPoint) > 0) {
          this.selectPoint(this.railway.railwayPoints.features[this.railway.railwayPoints.features.indexOf(this.railwayPoint) - 1]);
        }
        break;
      }
      case 'ArrowRight': {
        if (this.railway.railwayPoints.features.indexOf(this.railwayPoint) < this.railway.railwayPoints.features.length - 1) {
          this.selectPoint(this.railway.railwayPoints.features[this.railway.railwayPoints.features.indexOf(this.railwayPoint) + 1]);
        }
        event.stopPropagation();
      }
    }
  }

  updatePoint() {
    this.updateRailwayEditLayer();
    this.updatePointSelectLayer();
    // this.changeEvent.emit();
    this.railway.properties.pointsChanged = true;
  }

  colorChange(c) {
    this.railway.properties.color = c;
    this.saveRailwayProperties();
  }

  saveRailwayProperties() {
    this.projectService.updateProjectRailwayProperties(this.project, this.railway).subscribe();
  }

  cancelEdit() {
    delete this.railway.properties.editMode;
    this.saveRailwayProperties();
  }

}
