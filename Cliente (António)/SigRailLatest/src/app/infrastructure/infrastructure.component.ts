import { ElementpropertiesdialogComponent } from '../elementpropertiesdialog/elementpropertiesdialog.component';
import { RailwayElement, RailwayInfrastructure, Railway, Project } from '../model/project';
import { IconABC } from '../model/mapUtils';
import { Component, OnInit, Input, Output, OnDestroy, DoCheck, NgZone, EventEmitter,
  SimpleChanges, OnChanges, SimpleChange } from '@angular/core';
import { MatDialog } from '@angular/material';
// import { Feature, Point } from 'geojson';

import { ContextMenuService, ContextMenuComponent } from 'ngx-contextmenu';
// import { inspect } from 'util';

import {
  Map as LeafletMap, Layer, Icon, DivIcon, geoJSON, LatLng, Marker, FeatureGroup
} from 'leaflet';
import { ProjectService } from '../project.service';

@Component({
  selector: 'app-infrastructure',
  templateUrl: './infrastructure.component.html',
  styleUrls: ['./infrastructure.component.css']
})
export class InfrastructureComponent implements OnInit, OnDestroy, OnChanges, DoCheck {
  @Input() map: LeafletMap;
  @Input() project: Project;
  @Input() infrastructure: RailwayInfrastructure;
  @Input() elements: RailwayElement[];
  @Input() projectRailways: Railway[];
  @Input() elementMarkersMap: Map<RailwayElement, any>;
  @Input() mapLayerGroup: FeatureGroup;
  @Input() elementMenu: ContextMenuComponent;
  @Input() layerMenu: ContextMenuComponent;
  @Input() editMode: boolean;
  @Input() visible: boolean;
  @Input() color: string;


  @Output() saveEvent: EventEmitter<RailwayInfrastructure> = new EventEmitter<RailwayInfrastructure>();
  @Output() cancelEvent: EventEmitter<RailwayInfrastructure> = new EventEmitter<RailwayInfrastructure>();

//  mapLayerGroup = new FeatureGroup();
  parentRailway: Railway = null;

//  positionsClone = []; // positions backup copy

  classIconMap: Map<string, string> = new Map([['Estacao', 'fa-times'],
    ['Apiadeiro', 'fa-times-circle-o'],
    ['Passagem de nivel', 'fa-times-circle-o'],
    ['Site', 'fa-train'],
  ]);

  green2Icon = new Icon({
    iconUrl: 'assets/leaf-green.png',
    shadowUrl: 'assets/leaf-shadow.png',

    iconSize:     [38, 95], // size of the icon
    shadowSize:   [50, 64], // size of the shadow
    iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
    shadowAnchor: [4, 62],  // the same for the shadow
    popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
  });

  greenIcon = new IconABC();

  constructor(private projectService: ProjectService, private contextMenuService: ContextMenuService,
    private dialog: MatDialog, private zone: NgZone) { }

  ngOnInit() {
    if (this.infrastructure.properties.visible) { this.mapLayerGroup.addTo(this.map); }
    this.getParentRailway();
    if (this.elements && this.editMode) { this.setEditMode(); }
  }

  ngDoCheck() {
    if (this.infrastructure.properties.elementsChanged) {
      this.resetMarkers();
      delete this.infrastructure.properties.elementsChanged;
    }
    if (this.infrastructure.properties.approach) {
      this.map.fitBounds(this.mapLayerGroup.getBounds());
      delete this.infrastructure.properties.approach;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.elements && !changes.elements.isFirstChange()) {
      if (this.editMode) { this.setEditMode(); }
    }
    if (changes.editMode && !changes.editMode.isFirstChange()) {
      if (this.editMode) { this.setEditMode(); }// else { this.resetMarkers(); }
      this.resetMarkers();
    }
    if (changes.visible && !changes.visible.isFirstChange()) {
      if (this.visible) { this.mapLayerGroup.addTo(this.map); } else { this.mapLayerGroup.removeFrom(this.map); }
    }
    if ((changes.color && !changes.color.isFirstChange()) || (changes.elements && !changes.elements.isFirstChange())) {
      this.resetMarkers();
    }
  }

  ngOnDestroy(): void {
    this.mapLayerGroup.removeFrom(this.map);
  }

  getParentRailway() {
    if (this.infrastructure.properties.parentRailwayID) {
      let found = false;
      for (const r of this.projectRailways) {
        if (r.id === this.infrastructure.properties.parentRailwayID) {
          this.parentRailway = r;
          found = true;
        }
      }
      if (!found) {
        console.log(`Invalid railway link! (${this.infrastructure.name})`);
        delete this.infrastructure.properties.parentRailwayID;
      }
    }
  }

  resetMarkers() {
    this.mapLayerGroup.clearLayers();
    this.elementMarkersMap.clear();
    for (const element of this.elements) {
      const _comp = this;
      if (this.elementMarkersMap.get(element) == null) {
        const eMarker = geoJSON(element.location,
          { pointToLayer: function(feature, latlng) {
              // console.log('create marker: ' + element.name);
              const ei = new Marker(latlng, {
                icon: new IconABC({
                  color: _comp.color,
                  innerIcon: _comp.classIconMap.get(element.class),
                  selected: _comp.infrastructure.properties.editMode
                }),
                draggable: _comp.editMode,
                opacity: _comp.infrastructure.properties.opacity
              });
              ei.addEventListener('drag', (ev) => {
                feature.geometry.coordinates[0] = ev.target.getLatLng().lng;
                feature.geometry.coordinates[1] = ev.target.getLatLng().lat;
                // _comp.changeEvent.emit();
              });
              ei.addEventListener('dragend', () => {
                _comp.saveElement(element);
              });
              ei.addEventListener('click', () => {
              });
              if (_comp.editMode) {
                ei.on('contextmenu',  (ev) => {
                  _comp.zone.run( () => {
                    ev.originalEvent.preventDefault();
                    ev.originalEvent.stopPropagation();
                    _comp.contextMenuService.show.next({
                      contextMenu: _comp.elementMenu,
                      event: ev.originalEvent,
                      item: [_comp.infrastructure, element],
                    });
                  });
                });
              } else {
                ei.on('contextmenu',  (ev) => {
                  _comp.zone.run( () => {
                    ev.originalEvent.preventDefault();
                    ev.originalEvent.stopPropagation();
                    _comp.contextMenuService.show.next({
                      contextMenu: _comp.layerMenu,
                      event: ev.originalEvent,
                      item: _comp.infrastructure,
                    });
                  });
                });
              }
              ei.bindTooltip(_comp.infrastructure.name + '<br />' + element.name);
              return ei;
            }
          }).addTo(this.mapLayerGroup);
        this.elementMarkersMap.set(element, eMarker);
      }
    }
    if (this.visible) { this.mapLayerGroup.addTo(this.map); }
  }

  setEditMode() {
    // this.cloneElements();
    const _comp = this;
    this.elementMarkersMap.forEach((marker, element) => {
      if (this.editMode) {
        marker.on('contextmenu',  (ev) => {
          _comp.zone.run( () => {
            ev.originalEvent.preventDefault();
            ev.originalEvent.stopPropagation();
            _comp.contextMenuService.show.next({
              contextMenu: _comp.elementMenu,
              event: ev.originalEvent,
              item: [_comp.infrastructure, element],
            });
          });
        });
        marker.getLayers()[0].dragging.enable();
      } else {
        marker.on('contextmenu',  (ev) => {
          _comp.zone.run( () => {
            ev.originalEvent.preventDefault();
            ev.originalEvent.stopPropagation();
            _comp.contextMenuService.show.next({
              contextMenu: _comp.layerMenu,
              event: ev.originalEvent,
              item: _comp.infrastructure,
            });
          });
        });
        marker.getElement().draggable = false;
      }
    });
  }

/*  cloneElements() {
    console.log('clone elements');
    // this.elementsClone = this.elements.map(x => Object.assign({}, x));
    for (const element of this.elements) {
      this.positionsClone.push([element.location.geometry.coordinates[0], element.location.geometry.coordinates[1]]);
    }
  }

  revertElements() {
    console.log('revert elements');
    for (let i = 0; i < this.elements.length; i++) {
      for (const property in this.elements[i]) {
        if (property === 'location') {
          this.elements[i].location.geometry.coordinates =
            [this.positionsClone[i][0], this.positionsClone[i][1]];
        } else {
          // this.elements[i][property] = this.elementsClone[i][property];
        }
      }
    }
    this.resetMarkerPositions();
  }*/

  // resetMarkerPositions() {
  //   this.elementMarkersMap.forEach(function(marker, element) {
  //     const ll = new LatLng(element.location.geometry.coordinates[1], element.location.geometry.coordinates[0]);
  //     marker.getLayers()[0].setLatLng(ll);
  //   });
  // }

  newElement() {
    const center: LatLng = this.map.getCenter();
    const newElement: RailwayElement = {
      id: 0,
      class: 'Estacao',
      name: '',
      location: {
        type: 'Feature',
        geometry: {
          type: 'Point',
          coordinates: [center.lng, center.lat, 0]
        },
        properties: {
          kilometricPoint: 0
        }
      },
      properties: {
        visible: true
      }
    };
    const dialogRef = this.dialog.open(ElementpropertiesdialogComponent, {
      data: [this.infrastructure, newElement]
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        this.projectService.addProjectRailwayInfrastructureElement(this.project, this.infrastructure, newElement).subscribe(
        resp => {
          if (resp.status >= 200 && resp.status < 300) {
            this.projectService.getRailwayElement(resp.headers.get('Location')).subscribe( element => {
              this.elements.push(element);
              this.ngOnChanges({
                elements: new SimpleChange(null, this.elements, false)
              });
            });
          } else { alert('Error creating element:' + resp.statusText); }
        }
      );
      }
    });
  }

  saveElement(element: RailwayElement) {
    element.properties = JSON.stringify(element.properties);
    this.projectService.updateProjectRailwayInfrastructureElement(this.project, this.infrastructure, element).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          element.properties = JSON.parse(element.properties);
        } else { alert('Error saving element:' + resp.statusText); }
      },
      error => {
        alert('error:' + JSON.stringify(error.message));
      }
    );
  }

  saveInfrastructure() {
    // this.saveEvent.emit(this.infrastructure);
    this.projectService.updateProjectRailwayInfrastructureProperties(this.project, this.infrastructure).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          // element.properties = JSON.parse(element.properties);
        } else { alert('Error saving infrastructure:' + resp.statusText); }
      }
    );
  }

  cancel() {
//    this.revertElements();
    delete this.infrastructure.properties.editMode;
    this.saveInfrastructure();
    this.cancelEvent.emit(this.infrastructure);
  }
}
