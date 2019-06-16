import { Component, OnInit } from '@angular/core';
import { latLng, tileLayer, Map, marker, icon} from 'leaflet';
import { OBUService } from '../_services/obu.service';
import { OBU } from '../Model/OBU';
import { User } from '../Model/User';
import { LocalStorageService } from '../_services/localStorage.service';
import { Router } from '@angular/router';
import { OBUStatus } from '../Model/OBUStatus';
import * as L from 'leaflet';

@Component({
  selector: 'app-homemap',
  templateUrl: './homemap.component.html',
  styleUrls: ['./homemap.component.css']
})
export class HomemapComponent implements OnInit {

  constructor(
    private _obuService: OBUService,
    private router: Router,
    private _localStorage: LocalStorageService
  ) { }

  private obus: OBU[];
  private positions: OBUStatus[] = new Array();
  private user: User;
  private radioValue: number;
  private map : L.Map //Mapa a ser usado
  private markerArray = new Array; //Array de Markers

  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._obuService.getOBUs()
      .subscribe(obus => {
        this.obus = obus
        this.orderById()
        this.obus.forEach(obu => { //Em cada OBU, fazer o pedido das localizaçoes
          this._obuService.getPositionFromOBU(obu.id).subscribe( obuStatus =>{
            obuStatus.obuId = obu.id
            //console.log("obuStatus0:" + JSON.stringify(obuStatus))
            //console.log("position:0" + JSON.stringify(this.positions))
            this.positions.push(obuStatus)
            //console.log("obuStatus:" + JSON.stringify(obuStatus))
            //console.log("position:" +  JSON.stringify(this.positions))
          })
        });
        
      });
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

  
  onMapReady(map: L.Map) {
    this.map = map;
    this.map.setView([38.7573838, -9.1153841], 74)
    
    //var myMarker = marker([38.7573838, -9.1153841]).addTo(map);
    this.myMarker.bindPopup("<b>Hello world!</b><br>I am a popup, and this is ISEL!").openPopup();
  }

  orderById(){
    this.obus.sort( (h1,h2)=> h1.id - h2.id)
  }

  showLastPlace(){
    alert("showLastPlace");
    //this.map.setView([38.7573838, -9.1153841], 15);
    var latitude,longitude, coordenadas, newMarker
    this.obus.forEach(obu=>{
      coordenadas = this.positions.filter(pos => obu.id == pos.obuId )[0].coordinates
      if(coordenadas.length != 0){//Caso noa haja info das coords de certa obu, nada é feito
        latitude = coordenadas[coordenadas.length-1].latitude
        longitude = coordenadas[coordenadas.length-1].longitude
        newMarker = this.addNewMarkerToMap(latitude, longitude, obu.obuName)//cria o novo marker
        this.markerArray.push(newMarker)//Adiciona o marker ao array de markers
      }
      
    })
    
    var group = L.featureGroup(this.markerArray).addTo(this.map);
    this.map.fitBounds(group.getBounds());
  }

  showLast24h(){
    alert("showLast24h");
  }

  showLast48h(){
    alert("showLast48h");
  }

  showLast72h(){
    alert("showLast72h");
  }

  addNewMarkerToMap(latitude: number, longitude: number, msg: String){
    return new L.Marker([latitude, longitude], 
      {
        icon: icon({
          iconSize: [ 25, 41 ],
          iconAnchor: [ 13, 41 ],
          iconUrl: 'leaflet/marker-icon.png',
          shadowUrl: 'leaflet/marker-shadow.png'
        })
      }).
    bindPopup("<b>" + msg.toString() + "</b>").openPopup().addTo(this.map)
  }
  
}
