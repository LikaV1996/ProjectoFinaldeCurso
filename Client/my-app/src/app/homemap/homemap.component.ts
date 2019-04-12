import { Component, OnInit } from '@angular/core';

import { latLng, tileLayer, Map, marker, icon} from 'leaflet';


@Component({
  selector: 'app-homemap',
  templateUrl: './homemap.component.html',
  styleUrls: ['./homemap.component.css']
})
export class HomemapComponent implements OnInit {

  constructor() { }


  //TODO insert user data into mat-toolbar
  ngOnInit() {
  }
  
  myMarker = marker([38.7573838, -9.1153841], {
    icon: icon({
      iconSize: [ 25, 41 ],
      iconAnchor: [ 13, 41 ],
      iconUrl: 'leaflet/marker-icon.png',
      shadowUrl: 'leaflet/marker-shadow.png'
    })
  });


  private options = {
    layers: [
      tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
      }),
    this.myMarker
    ],
    zoom: 7,
    center: latLng([38.7573838, -9.1153841])
  };

  
  onMapReady(map: Map) {
    map.setView([38.7573838, -9.1153841], 74)
    
    //var myMarker = marker([38.7573838, -9.1153841]).addTo(map);
    this.myMarker.bindPopup("<b>Hello world!</b><br>I am a popup, and this is ISEL!").openPopup();
  }
}
