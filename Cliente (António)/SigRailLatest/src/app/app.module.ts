import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgProgressModule } from '@ngx-progressbar/core';
import { NgProgressHttpModule } from '@ngx-progressbar/http';

import { LeafletModule } from '@asymmetrik/ngx-leaflet'; // Leaflet

// import * as sdbscan from 'sdbscan';
// const dobbyscan = require('dobbyscan');

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCoffee, faBars, faBell, faUserCircle, faUser, faSignOutAlt,
  faFolderOpen, faWifi, faUsers, faCogs, faLayerGroup, faProjectDiagram,
  faMapMarker, faTrain, faBuilding, faAdjust, faEyeSlash, faPencilAlt, faTimes, faShare, faBacon,
  faFile, faSave, faSlidersH, faTrash, faWindowClose, faSearch, faEye, faCopy,
  faUpload, faLock, faQuestion, faFolderPlus, faEdit, faTimesCircle, faLockOpen, faShareSquare, faMapMarked, faAngleUp, faAngleDown
} from '@fortawesome/free-solid-svg-icons';
import { faEdit as farEdit, faObjectGroup
} from '@fortawesome/free-regular-svg-icons';

import { ContextMenuModule } from 'ngx-contextmenu';
import { ColorPickerModule } from 'ngx-color-picker';

import {MatButtonModule,
  MatTooltipModule,
  MatCheckboxModule,
  MatSelectModule,
  MatRadioModule,
  MatMenuModule,
  MatToolbarModule,
  MatTableModule,
  MatFormFieldModule,
  MatInputModule,
  MatSidenavModule,
  MatSliderModule,
  MatListModule,
  MatIconModule,
  // MatIconRegistry,
  MatDialogModule,
  MatSnackBarModule,
  MatCardModule,
  MatExpansionModule,
  MatTabsModule,
  MatProgressSpinnerModule,
  MatProgressBarModule,
  } from '@angular/material'; // Material

import { AppComponent } from './app.component';
import { AppService } from './app.service';
import { ProjectService } from './project.service';
import { AuthService } from './auth.service';
import { AuthGuard } from './auth.guard';

import { HomepageComponent } from './homepage/homepage.component';
import { LogoComponent } from './logo/logo.component';
import { FooterinfoComponent } from './footerinfo/footerinfo.component';
import { ProjectlistComponent } from './projectlist/projectlist.component';
import { LoginComponent } from './login/login.component';
import { ProjectComponent } from './project/project.component';
import { RailwayComponent } from './railway/railway.component';
import { RailwayeditComponent } from './railwayedit/railwayedit.component';
import { LayerpropertiesdialogComponent } from './layerpropertiesdialog/layerpropertiesdialog.component';
import { PointeditdialogComponent } from './pointeditdialog/pointeditdialog.component';
import { RailwaypointComponent } from './railwaypoint/railwaypoint.component';
import { BuildinggroupComponent } from './buildinggroup/buildinggroup.component';
import { BuildingComponent } from './building/building.component';
import { InfrastructureComponent } from './infrastructure/infrastructure.component';
import { ElementpropertiesdialogComponent } from './elementpropertiesdialog/elementpropertiesdialog.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { ProjectdetailsdialogComponent } from './projectdetailsdialog/projectdetailsdialog.component';
import { PEDetailsDialogComponent } from './pedetails-dialog/pedetails-dialog.component';
import { LayerlistComponent } from './layerlist/layerlist.component';
import { BuildingpropertiesdialogComponent } from './buildingpropertiesdialog/buildingpropertiesdialog.component';
import { ProjectlistdialogComponent } from './projectlistdialog/projectlistdialog.component';
import { LayerlistdialogComponent } from './layerlistdialog/layerlistdialog.component';

const appRoutes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    data: { title: 'Login' }
  },
  {
    path: 'home',
    component: HomepageComponent,
     children: [
       { path: '', redirectTo: 'projects', pathMatch: 'full' },
       { path: 'projects', component: ProjectlistComponent},
       { path: 'layers', component: LayerlistComponent}
     ],
     data: { title: 'Home' },
     canActivate: [AuthGuard]
   },
  {
    path: 'project/:id',
    component: ProjectComponent,
    data: { title: 'Project' },
    canActivate: [AuthGuard]
  },
   { path: '',
     redirectTo: '/home/projects',
     pathMatch: 'full'
   },
 ];

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    LogoComponent,
    FooterinfoComponent,
    ProjectlistComponent,
    LoginComponent,
    ProjectComponent,
    RailwayComponent,
    RailwayeditComponent,
    LayerpropertiesdialogComponent,
    PointeditdialogComponent,
    RailwaypointComponent,
    BuildinggroupComponent,
    BuildingComponent,
    InfrastructureComponent,
    ElementpropertiesdialogComponent,
    ConfirmDialogComponent,
    ProjectdetailsdialogComponent,
    PEDetailsDialogComponent,
    LayerlistComponent,
    BuildingpropertiesdialogComponent,
    ProjectlistdialogComponent,
    LayerlistdialogComponent,
  ],
  entryComponents: [
    LayerpropertiesdialogComponent,
    PointeditdialogComponent,
    ElementpropertiesdialogComponent,
    ConfirmDialogComponent,
    ProjectdetailsdialogComponent,
    PEDetailsDialogComponent,
    BuildingpropertiesdialogComponent,
    ProjectlistdialogComponent,
    LayerlistdialogComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(
      appRoutes,
//      { enableTracing: true } // <-- debugging purposes only
    ),
    ContextMenuModule.forRoot({
      autoFocus: true,
    }),
    FormsModule,
    HttpClientModule,
    NgProgressModule,
    NgProgressHttpModule,

    LeafletModule.forRoot(),

    FontAwesomeModule,

    ColorPickerModule,

    MatButtonModule, MatCheckboxModule, MatRadioModule,
    MatTooltipModule,
    MatSelectModule,
    MatMenuModule,
    MatToolbarModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSliderModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatDialogModule,
    MatSnackBarModule,
    MatCardModule,
    MatExpansionModule,
//    MatColorPickerModule,
    MatTabsModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
  ],
  providers: [
    AppService,
    ProjectService,
    AuthService,
    AuthGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor() {
    library.add( faCoffee, faBacon,
      faBars, faBell, faUserCircle, faUser, faSignOutAlt,
      faFolderOpen, faWifi, faUsers, faCogs, faLayerGroup, faProjectDiagram,
      faTrain, faBuilding, faAdjust, faMapMarker, faMapMarked, faObjectGroup,
      faEyeSlash, faPencilAlt, faTimes, faShare,
      faFile, faSave, faSlidersH, faTrash, faWindowClose,
      faSignOutAlt, faQuestion, faFolderPlus, faEdit, farEdit,
      faSearch, faEye, faEyeSlash, faCopy, faUpload, faLock, faLockOpen,
      faShareSquare, faFolderOpen, faObjectGroup,
      faTimesCircle, faAngleUp, faAngleDown);
  }
}
