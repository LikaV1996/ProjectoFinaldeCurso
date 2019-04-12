import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';

import {DemoMaterialModule} from 'material-module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatNativeDateModule, MatToolbarModule, MatButtonModule, MatSidenavModule, MatIconModule, MatListModule} from '@angular/material';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { UserComponent } from './user/user.component';

import { HttpClientModule }    from '@angular/common/http';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { UserDetailComponent } from './user-detail/user-detail.component';

import { CookieService } from "angular2-cookie/services/cookies.service";

import { HomemapComponent } from './homemap/homemap.component';
import { HttpinterceptorModule } from './httpinterceptor/httpinterceptor.module';
import { NavMenuComponent } from './nav-menu/nav-menu.component';
import { LayoutModule } from '@angular/cdk/layout';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UserComponent,
    UserDetailComponent,
    HomemapComponent,
    NavMenuComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    DemoMaterialModule,
    FormsModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    LeafletModule.forRoot(),
    HttpinterceptorModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule
  ],
  providers: [CookieService],
  bootstrap: [AppComponent]
})

export class AppModule { }
