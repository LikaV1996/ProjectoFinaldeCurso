import { Component, OnInit, ViewChild } from '@angular/core';
import { latLng, tileLayer, Map, marker, icon} from 'leaflet';
import { OBUService } from '../_services/obu.service';
import { OBU } from '../Model/OBU';
import { User } from '../Model/User';
import { LocalStorageService } from '../_services/localStorage.service';
import { Router } from '@angular/router';
import { OBUStatus } from '../Model/OBUStatus';
import * as L from 'leaflet';
//import { Chart } from 'chart.js';


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

  /*
  @ViewChild('lineChart') private chartRef; 
  chart :any;
  chartPoints = [{  
      x: new Date(),  
      y: 10
    },{  
      x: new Date(),  
      y: 20
  }];
  */
  private obus: OBU[];
  private positions= new Array() //Todas as posiçoes
  private filteredPositions: Positions[] = new Array()
  private user: User;
  private map : L.Map //Mapa a ser usado
  private polylines: L.Polyline[] = new Array();
  private layerGroup
  private startDate = null
  private endDate = null
  private toogleButtonVisible : boolean = false

  ngOnInit() {
    this.user = this._localStorage.getCurrentUserDetails()

    this._obuService.getOBUs()
      .subscribe(obus => {
        this.obus = obus
        this.orderById()
        this.obus.forEach(obu => { //Em cada OBU, fazer o pedido das localizaçoes
          this._obuService.getPositionFromOBU(obu.id,null,null).subscribe( obuStatus =>{
            if(obuStatus.length != 0){//Caso a OBU nao tenha a obuStatus
              var aux = new Positions()
              aux.obuId = obu.id
              aux.obuName = obu.obuName
              aux.obuStatus = obuStatus
              this.positions.push(aux)
            }
           
          })
        });
        
      });


      /*
      this.chart = new Chart(this.chartRef.nativeElement, {
        type: 'line',
        data: {
          //labels: ["2015-03-15", "2015-03-25T13:02:00Z", (new Date().getDate())+"-"+(new Date().getMonth()+1)+""], // your labels array
          datasets: [
            {
              data: this.chartPoints, // your data array
              borderColor: '#00AEFF',
              fill: false
            }
          ]
        },
        options: {
          legend: {
            display: true
          },
          scales: {
            xAxes: [{
              display: true,
              type: 'time',
                time: { //nao faz nada
                  displayFormats: {
                    quarter: 'MMM D'
                  }
                }
            }],
            yAxes: [{
              display: true
            }],
          }
        }
      });
      */
   
  }

  welcomeMarker = marker([38.7573838, -9.1153841], {
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
      })
    ],
    zoom: 7,
    center: latLng([38.7573838, -9.1153841])
  };

  
  onMapReady(map: L.Map) {
    this.map = map;
    this.map.setView([38.7573838, -9.1153841], 74)
    this.layerGroup = L.featureGroup().addTo(this.map);
    this.welcomeMarker.addTo(this.layerGroup);
    this.welcomeMarker.bindPopup("<b>ISEL / SOLVIT</b><br>Choose your options!").openPopup();
    
  }

  orderById(){
    this.obus.sort( (h1,h2)=> h1.id - h2.id)
  }

  showLastPlace(){
    this.cleanMap()
    this.toogleButtonVisible = false

    //Criar Markers correspondentes ao sitio de cada obu
    var latitude,longitude, coordenadas, newMarker
    this.positions.forEach(pos=>{
      coordenadas = this.getLastDate(pos.obuStatus)
      latitude = coordenadas.location.lat
      longitude = coordenadas.location.lon
      newMarker = this.NewMarkerToMap(latitude, longitude, pos.obuName)
      
      newMarker.addTo(this.layerGroup); //Adiciona o marker ao layerGroup
    })

    this.map.fitBounds(this.layerGroup.getBounds().pad(0.5));
  }

  async showPlaces(){
    this.cleanMap()
    this.toogleButtonVisible = true

    await Promise.all(
      this.obus.map(obu =>  //Em cada OBU, fazer o pedido das localizaçoes
        this._obuService.getPositionFromOBU(obu.id,this.startDate,this.endDate).toPromise().then( obuStatus =>{
          if(obuStatus.length != 0){//Caso a OBU nao tenha a obuStatus
            var aux = new Positions()
            aux.obuId = obu.id
            aux.obuName = obu.obuName
            aux.obuStatus = obuStatus
            var found = false
            this.filteredPositions.forEach( obj => {
              if(obj.obuId == obu.id){
                obj.obuStatus = obuStatus
                found = true
              }
            })
            if (!found) this.filteredPositions.push(aux)
          }
        })
      ))

    var pointList, newPolyline
    this.filteredPositions.forEach(pos=>{
      if(pos.visible){
      
        if(pos.obuStatus.length >= 2){ //Tem de haver pelo menos 2 pontos
          pointList = this.getPointList(pos.obuStatus)
          newPolyline = this.NewPolylineToMap(pointList,pos.obuName)
          this.polylines.push(newPolyline);
        }
      }
    })

    this.polylines.forEach(line =>{ //Activa as polylines
      this.map.addLayer(line);
    })
    
    if(this.polylines.length != 0)
      this.map.fitBounds(this.layerGroup.getBounds().pad(0.5));
    
  }

  NewMarkerToMap(latitude: number, longitude: number, msg: String){
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

  NewPolylineToMap(pointList: L.LatLng[], obuName: string){

    var firstpolyline = new L.Polyline(pointList,{
    color: 'red',
    weight: 3,
    opacity: 0.5,
    smoothFactor: 1
    });

    var newMarker = this.NewMarkerToMap(pointList[0].lat,pointList[0].lng,obuName+" End")
    newMarker.addTo(this.layerGroup);
    var newMarker1 = this.NewMarkerToMap(pointList[pointList.length-1].lat,pointList[pointList.length-1].lng,obuName+" Start")
    newMarker1.addTo(this.layerGroup);

    return firstpolyline
  }

  cleanMap(){
    //Limpar Mapa de Polylines e Markers
    this.layerGroup.clearLayers();
    
    this.polylines.forEach(line =>{ 
      this.map.removeLayer(line);
    })
    this.polylines = new Array()
    this.filteredPositions.forEach( obj =>{
      obj.obuStatus = new Array()
    })
    
  }


  getLastDate(obuStatus:OBUStatus[]){ //retorna o indice com a data mais recente

    obuStatus.sort(function(a,b){
      var a_data = new Date(a.date.toString());
      var b_data = new Date(b.date.toString());
      return b_data.getTime() - a_data.getTime();
    });
    return obuStatus[0]

  }

  getPointList(obustatus:OBUStatus[]){

    var pointList = new Array(), newPoint
    obustatus.forEach( _obustatus=>{
      newPoint = new L.LatLng(_obustatus.location.lat, _obustatus.location.lon);
      pointList.push(newPoint)
    })
    return pointList
  }

  updateMap(event, obuId:number){
    
    this.filteredPositions.forEach(obj=>{
      if(obj.obuId == obuId){
        obj.visible = event.currentTarget.checked
      }
    })
    this.showPlaces()
  }
  

  
}

class Positions{ //Classe auxiliar
  obuId: number
  obuName: string
  obuStatus: OBUStatus[]
  visible: boolean = true
}
