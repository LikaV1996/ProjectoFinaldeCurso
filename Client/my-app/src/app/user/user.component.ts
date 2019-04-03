import { Component, OnInit } from '@angular/core';
import { User } from '../Model/User';
import { Users } from '../Model/Users';
import { UserService } from '../user.service';

import {Router} from '@angular/router';

import { latLng, tileLayer, Map, marker, icon} from 'leaflet';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  private token: string;

  constructor(
    private router: Router,
    private userService: UserService
  ) {
    const navigation = this.router.getCurrentNavigation();
    const state = navigation.extras.state as {userToken: string};
    this.token = state.userToken;
   }

  private users: User[];
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
  ngOnInit() {
    console.log(this.token)
    this.userService.getUsers(this.token)
    .subscribe(users => {
      console.log(users)
      this.users = users.users
    });

  }
  

  onMapReady(map: Map) {
    map.setView([38.7573838, -9.1153841], 74)

    //var myMarker = marker([38.7573838, -9.1153841]).addTo(map);
    this.myMarker.bindPopup("<b>Hello world!</b><br>I am a popup, and this is ISEL!").openPopup();
  }

}
