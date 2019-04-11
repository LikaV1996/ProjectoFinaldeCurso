//
// Simple Component that generates/updates a Polyline from a Railway
//
import { Railway } from '../model/project';
import { Component, OnInit, Input, OnDestroy, NgZone, DoCheck } from '@angular/core';

import { Map as LeafletMap, Polyline } from 'leaflet';

import { ContextMenuService, ContextMenuComponent } from 'ngx-contextmenu';


@Component({
  selector: 'app-railway',
  template: ''
})

export class RailwayComponent implements OnInit, OnDestroy, DoCheck {
  @Input() map: LeafletMap;
  @Input() polylinesMap: Map<Railway, Polyline>;
  @Input() railway: Railway;
  @Input() contextMenu: ContextMenuComponent;

  private polyline: Polyline;

  private polyStyle: any;

  constructor(private contextMenuService: ContextMenuService, private zone: NgZone) { }

  ngOnInit() {
    if ('railwayPoints' in this.railway) {
      this.createPolyline();
      this.polyStyle = {
        weight: this.railway.properties.weight,
        opacity: this.railway.properties.opacity,
        color: this.railway.properties.color,
      };
    } else {
      console.log('no RailwayPoints.');
    }
  }

  ngDoCheck() {
     if (this.railway.properties.color !== this.polyStyle.color
        || this.railway.properties.weight !== this.polyStyle.weight
        || this.railway.properties.opacity !== this.polyStyle.opacity) {
      this.getPolyStyle();
      this.polyline.setStyle(this.polyStyle);
    }
    if (this.railway.properties.pointsChanged) {
      this.updatePolyLine(this.railway);
      delete this.railway.properties.pointsChanged;
    }
  }

  ngOnDestroy(): void {
    console.log('destroy railway: ' + this.railway.name);
    this.polyline.removeFrom(this.map);
    this.polylinesMap.delete(this.railway);
  }

  createPolyline() {
    // cria a polyline
    const pointsLatLng = [];
    for (const p of this.railway.railwayPoints.features) {
      pointsLatLng.push({lat: p.geometry.coordinates[1], lng: p.geometry.coordinates[0]});
    }
    this.getPolyStyle();
    this.polyline = new Polyline(pointsLatLng, this.polyStyle);
    const _comp = this;
    const rw = this.railway;
    this.polyline.on('contextmenu', (ev) => {
      _comp.zone.run( () => {
        ev.originalEvent.preventDefault();
        ev.originalEvent.stopPropagation();
        _comp.contextMenuService.show.next({
          contextMenu: _comp.contextMenu,
          event: ev.originalEvent,
          item: rw,
        });
      });
    });
    this.polylinesMap.set(this.railway, this.polyline);
    this.polyline.addTo(this.map);
  }

  updatePolyLine(railway: Railway) {
    if (!railway.properties.visible) { this.polyline.removeFrom(this.map);
    } else { this.polyline.addTo(this.map); }
    // actualiza a polyline
    const pointsLatLng = [];
    for (const p of railway.railwayPoints.features) {
      pointsLatLng.push({lat: p.geometry.coordinates[1], lng: p.geometry.coordinates[0]});
    }
    this.getPolyStyle();
    this.polyline.setStyle(this.polyStyle);
    this.polyline.setLatLngs(pointsLatLng);
  }

  getPolyStyle() {
    this.polyStyle = {
      weight: this.railway.properties.weight,
      opacity: this.railway.properties.opacity,
      color: this.railway.properties.color,
    };
  }
}
