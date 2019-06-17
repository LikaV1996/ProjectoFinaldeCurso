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
  private positions= new Array()//: OBUStatus[][] = new Array<OBUStatus[]>();
  private user: User;
  private radioValue: number;
  private map : L.Map //Mapa a ser usado
  private markerArray = new Array; //Array de Markers
  private group //grp de markers
  private polylines: L.Polyline[] = new Array();
  private obusToShow= new Array(); //id das obus a mostrar
  private layerGroup

  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._obuService.getOBUs()
      .subscribe(obus => {
        this.obus = obus
        this.orderById()
        this.obus.forEach(obu => { //Em cada OBU, fazer o pedido das localizaçoes
          this._obuService.getPositionFromOBU(obu.id).subscribe( obuStatus =>{
            if(obuStatus.length != 0){//Caso a OBU nao tenha a obuStatus
              var aux = new Positions()
              aux.obuId = obu.id
              aux.obuName = obu.obuName
              aux.obuStatus = obuStatus
              this.positions.push(aux)
              this.obusToShow.push(obu.id)  
            }
           
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
    this.layerGroup = L.featureGroup().addTo(this.map);
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
    this.positions.forEach(pos=>{
      coordenadas = this.getLastDate(pos.obuStatus)
      latitude = coordenadas.location.lat
      longitude = coordenadas.location.lon
      newMarker = this.addNewMarkerToMap(latitude, longitude, pos.obuName)
      
      newMarker.addTo(this.layerGroup);
      //this.markerArray.push(newMarker)//Adiciona o marker ao array de markers
    })

    //this.group = L.featureGroup(this.markerArray).addTo(this.map); //grp de markers
    //this.map.fitBounds(this.group.getBounds());
    this.map.fitBounds(this.layerGroup.getBounds());
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
    /* TODO!!!! - CONFIRM
    if(confirm("showLast72h"))
      alert("showLast72h");
    */  
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

    var newMarker = this.addNewMarkerToMap(pointA.lat,pointA.lng,"OBU0 Start")
    newMarker.addTo(this.layerGroup);
    var newMarker1 = this.addNewMarkerToMap(pointB.lat,pointB.lng,"OBU0 End")
    newMarker1.addTo(this.layerGroup);
    
    //this.markerArray.push(this.addNewMarkerToMap(pointA.lat,pointA.lng,"OBU0 Start"))
    //this.markerArray.push(this.addNewMarkerToMap(pointB.lat,pointB.lng,"OBU0 End"))
    //this.map.setView([28.635308, 77.22496], 15);

    //this.group = L.featureGroup(this.markerArray).addTo(this.map); //grp de markers
    //this.map.fitBounds(this.group.getBounds());
    this.map.fitBounds(this.layerGroup.getBounds());
  }

  cleanMap(){
    //Limpar Mapa de Polylines e Markers
    /*
    if(this.group != null)
      this.group.clearLayers(); 
    */  
    this.layerGroup.clearLayers();

    this.polylines.forEach(line =>{ 
      this.map.removeLayer(line);
    })
    
    this.markerArray = new Array();
    //this.group = L.featureGroup(this.markerArray).addTo(this.map);
    
  }


  getLastDate(obuStatus:OBUStatus[]){ //retorna o indice com a data mais recente

    obuStatus.sort(function(a,b){
      var a_data = new Date(a.date.toString());
      var b_data = new Date(b.date.toString());
      return b_data.getTime() - a_data.getTime();
    });
    return obuStatus[0]

  }

  updateMap(event, obuId:number){
    
    //alert(event.currentTarget.checked)
    this.cleanMap()
    if(!event.currentTarget.checked){ //está a ir para false
      this.obusToShow = this.obusToShow.filter(c=>c!== obuId)
      var latitude,longitude, coordenadas, newMarker
      this.positions.forEach(pos=>{
        if(this.obusToShow.some(curr=>curr === pos.obuId)){
          coordenadas = this.getLastDate(pos.obuStatus)
          latitude = coordenadas.location.lat
          longitude = coordenadas.location.lon
          newMarker = this.addNewMarkerToMap(latitude, longitude, pos.obuName)
          //this.markerArray.push(newMarker)//Adiciona o marker ao array de markers
          newMarker.addTo(this.layerGroup);//Adiciona o marker ao layerGroup
        }
      })

      /*
      this.obusToShow = this.obusToShow.reduce((acc,curr)=>{
        if(curr === obuId)
          return acc
        
          acc.push(curr)
        let pos = this.positions.find(c=>c.obuId == curr)
        if(pos){
          var 
          coordenadas = this.getLastDate(pos.obuStatus),
          latitude = coordenadas.location.lat,
          longitude = coordenadas.location.lon, 
          newMarker = this.addNewMarkerToMap(latitude, longitude, pos.obuName)

          this.markerArray = [...this.markerArray,newMarker]//Adiciona o marker ao array de markers
        }
        return acc
      },[])
      */
      
      


    }else{ //se esta a ir para true
      
    }
         
  }


  
}

class Positions{ //Classe auxiliar
  obuId: number
  obuName: string
  obuStatus: OBUStatus[]
}
