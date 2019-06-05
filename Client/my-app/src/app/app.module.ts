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
import { OBUComponent } from './obu/obu.component';
import { LogoutComponent } from './logout/logout.component';
import { ObuDetailComponent } from './obu-detail/obu-detail.component';
import { ConfigurationComponent } from './configuration/configuration.component';
import { ConfigurationDetailComponent } from './configuration-detail/configuration-detail.component';
import { TestPlansComponent } from './test-plans/test-plans.component';
import { HardwareComponent } from './hardware/hardware.component';
import { HardwareDetailComponent } from './hardware-detail/hardware-detail.component';
import { HardwareCreateComponent } from './hardware-create/hardware-create.component';
import { ObuCreateComponent } from './obu-create/obu-create.component';
import { ConfigurationCreateComponent } from './configuration-create/configuration-create.component';
import { TestPlansCreateComponent } from './test-plans-create/test-plans-create.component';
import { TestPlansDetailComponent } from './test-plans-detail/test-plans-detail.component';
import { SetupComponent } from './setup/setup.component';
import { ServerlogComponent } from './serverlog/serverlog.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UserComponent,
    UserDetailComponent,
    HomemapComponent,
    NavMenuComponent,
    OBUComponent,
    LogoutComponent,
    ObuDetailComponent,
    ConfigurationComponent,
    ConfigurationDetailComponent,
    TestPlansComponent,
    HardwareComponent,
    HardwareDetailComponent,
    HardwareCreateComponent,
    ObuCreateComponent,
    ConfigurationCreateComponent,
    TestPlansCreateComponent,
    TestPlansDetailComponent,
    SetupComponent,
    ServerlogComponent
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
