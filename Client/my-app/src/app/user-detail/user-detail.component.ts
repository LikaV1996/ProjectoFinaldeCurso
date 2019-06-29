import { Component, OnInit, Input } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import { Injectable } from '@angular/core';
import { User } from '../Model/User';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Routes } from "../httproutes"
import {Router, NavigationExtras} from '@angular/router';
import { UserService } from '../_services/user.service';
import {Location} from '@angular/common';
import { UserHasOBU } from '../Model/UserHasOBU';
import { UserHasObuService } from '../_services/userHasObu.service';
import { OBUService } from '../_services/obu.service';
import { OBU } from '../Model/OBU';

let routes = new Routes

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  @Input() user: User;


  private id: number;//id do user que está a ser visto
  private user_has_obu: UserHasOBU[];
  private obuToAddId: number;
  private obus: OBU[];

  private selectedRole: string = "VIEWER";

  constructor(
    private router: Router,
    private _userService: UserService,
    private _userHasObuService: UserHasObuService,
    private _obuService: OBUService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private _location: Location
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    
    this._userService.getUserByParam(this.id).subscribe(user => {
     this.user = user

      this._userHasObuService.getUserObus(this.id).subscribe(userobus =>{
        this.user_has_obu = userobus
      })

      this._obuService.getOBUs().subscribe(obus => {
        this.obus = obus
      })
    })

  }

  goBack(){
    this._location.back();
  }

  saveChanges(){
    this._userService.updateUser(this.user.id, this.user.userProfile, this.user.suspended).subscribe(user => {
      this.user = user
      alert("User updated!")
      this.goBack()
    })
  }

  
  addObuToUser(obuId:number, userId:number){
    if(obuId==null)
      alert('You must choose a configuration!')

    if(confirm("This will save immediately, do you want to continue?")){  
      this._userHasObuService.addObuToUser(obuId,userId, this.selectedRole).subscribe(
          data =>{
            //sucesso
            alert('OBU associated sucessfully')
            this._userHasObuService.getUserObus(this.id).subscribe(userobus =>{
            this.user_has_obu = userobus
          })
          },
          error => alert(error.error.detail) //erro
        )
    }
  }

  deleteObuFromUser(obuId:number, userId:number){
    if(confirm("This will save immediately, do you want to continue?")){
      this._userHasObuService.deleteObuFromUser(obuId, userId).subscribe(_ =>{
        alert('OBU disassociated sucessfully')
        this._userHasObuService.getUserObus(this.id).subscribe(userobus =>{
          this.user_has_obu = userobus
        })
      })
    }  
  }

  setViewer(){ this.selectedRole = "VIEWER"}
  setEditor(){ this.selectedRole = "EDITOR"}

}
