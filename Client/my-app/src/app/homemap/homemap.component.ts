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
  private group //grp de markers
  private polylines: L.Polyline[] = new Array();


  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._obuService.getOBUs()
      .subscribe(obus => {
        this.obus = obus
        this.orderById()
        this.obus.forEach(obu => { //Em cada OBU, fazer o pedido das localizaçoes
          this._obuService.getPositionFromOBU(obu.id).subscribe( obuStatus =>{
            obuStatus.obuId = obu.id
            this.positions.push(obuStatus)
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
    //alert("showLastPlace");

    this.cleanMap()
    
    //Criar Markers correspondentes ao sitio de cada obu
    var latitude,longitude, coordenadas, newMarker
    this.obus.forEach(obu=>{
      coordenadas = this.positions.filter(pos => obu.id == pos.obuId )[0][0].location
      if(coordenadas != null){//Caso nao haja info das coords de certa obu, nada é feito
        latitude = coordenadas.lat
        longitude = coordenadas.lon
        newMarker = this.addNewMarkerToMap(latitude, longitude, obu.obuName)//cria o novo marker
        this.markerArray.push(newMarker)//Adiciona o marker ao array de markers
      }
      
    })

    this.group = L.featureGroup(this.markerArray).addTo(this.map); //grp de markers
    this.map.fitBounds(this.group.getBounds());
  }

  showLast24h(){
    alert("showLast24h");
    this.addLinePolyineToMap(null)
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

  addLinePolyineToMap(points: L.LatLng[]){

    this.cleanMap()

    var pointA = new L.LatLng(28.635308, 77.22496);
    var pointB = new L.LatLng(28.984461, 77.70641);
    var pointList = [pointA, pointB];

    var firstpolyline = new L.Polyline(pointList,{
    color: 'red',
    weight: 3,
    opacity: 0.5,
    smoothFactor: 1
    });
    
    this.polylines.push(firstpolyline);
    this.polylines.forEach(line =>{
      this.map.addLayer(line);
    })

    
    this.markerArray.push(this.addNewMarkerToMap(pointA.lat,pointA.lng,"OBU0 Start"))
    this.markerArray.push(this.addNewMarkerToMap(pointB.lat,pointB.lng,"OBU0 End"))
    //this.map.setView([28.635308, 77.22496], 15);

    this.group = L.featureGroup(this.markerArray).addTo(this.map); //grp de markers
    this.map.fitBounds(this.group.getBounds());
  }

  cleanMap(){
    //Limpar Mapa de Polylines e Markers
    if(this.group != null)
      this.group.clearLayers();

    this.polylines.forEach(line =>{ 
      this.map.removeLayer(line);
    })
    
    this.markerArray = new Array();
  }
  
}
