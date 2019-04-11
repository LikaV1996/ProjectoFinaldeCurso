declare var require: any
import { Component, OnInit, ViewChild, OnDestroy, NgZone, QueryList, ViewChildren } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs';
import { switchMap, finalize } from 'rxjs/operators';
import { inspect } from 'util';

const dobbyscan = require('dobbyscan');

import { AppService } from '../app.service';
import { ProjectService } from '../project.service';

import { BaseLayer, Railway, Project, RailwayInfrastructure, RailwayElement,
  Clutter, Building, BuildingGroup, PropagationEnvironment, Tunnel} from '../model/project';
import { TileLayerH, Deflate} from '../model/mapUtils';

import { MatSnackBar, MatDialog, MatMenuTrigger } from '@angular/material';
import { NgProgressRef } from '@ngx-progressbar/core';
import { Point, Feature } from 'geojson';
import {
  TileLayer, LatLng, Map as LeafletMap,
  FeatureGroup, Polyline, Layer
} from 'leaflet';
import { ContextMenuService, ContextMenuComponent } from 'ngx-contextmenu';

import { RailwayeditComponent } from '../railwayedit/railwayedit.component';
import { LayerpropertiesdialogComponent } from '../layerpropertiesdialog/layerpropertiesdialog.component';
import { PointeditdialogComponent } from '../pointeditdialog/pointeditdialog.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { ProjectdetailsdialogComponent } from '../projectdetailsdialog/projectdetailsdialog.component';
import { PEDetailsDialogComponent } from '../pedetails-dialog/pedetails-dialog.component';
import { ElementpropertiesdialogComponent } from '../elementpropertiesdialog/elementpropertiesdialog.component';
import { BuildingpropertiesdialogComponent } from '../buildingpropertiesdialog/buildingpropertiesdialog.component';
import { ProjectlistdialogComponent } from '../projectlistdialog/projectlistdialog.component';
import { LayerlistdialogComponent } from '../layerlistdialog/layerlistdialog.component';
import { getCentroid, giftWrap, graham, monotoneChain } from '../model/geoUtils';


@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})


export class ProjectComponent implements OnInit, OnDestroy {
  menuSubscription: Subscription; // APP Menu subscription
  @ViewChild(MatMenuTrigger) contextMenu: MatMenuTrigger;
  @ViewChild('elementContextMenu') elementContextMenu: ContextMenuComponent;
  @ViewChild('layerContextMenu') layerContextMenu: ContextMenuComponent;
  @ViewChild('pointContextMenu') pointContextMenu: ContextMenuComponent;
  @ViewChild('buildingPointContextMenu') buildingPointContextMenu: ContextMenuComponent;
  @ViewChildren(RailwayeditComponent) railwayEdit: QueryList<RailwayeditComponent>;
  raiwayEdit = false;

  progressRef: NgProgressRef;

  project: Project;
  projectDetails = {
    railwayCount: 0,
    clutterCount: 0,
    infrastructureCount: 0,
    buildingGroupCount: 0,
  };

  projectPE: PropagationEnvironment;

  layers: BaseLayer[] = [];

  railways: Railway[];
  railwayPolylines = new Map<Railway, Polyline>();

  peLayers: BaseLayer[];

  clutters: Clutter[];
  clutterClasses = new Map<Clutter, string[]>();
  clutterMapLayers = new Map<Clutter, TileLayer>();

  infrastructures: RailwayInfrastructure[] = [];
  infrastructureElements = new Map<RailwayInfrastructure, RailwayElement[]>();
  infrastructureTunnels = new Map<RailwayInfrastructure, Tunnel[]>();
  infrastructureElementsLayersMap = new Map<RailwayInfrastructure, Map<RailwayElement, Layer>>();
  infrastructureMapLayerGroups = new Map<RailwayInfrastructure, FeatureGroup>();

  buildingGroups: BuildingGroup[] = [];
  buildings = new Map<BuildingGroup, Building[]>();
  buildingsMapLayerGroups = new Map<BuildingGroup, /*FeatureGroup*/ Deflate>();

  // MapBox Color
  mbAttr = 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
      '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
      'Imagery � <a href="http://mapbox.com">Mapbox</a>';
  mbUrl = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=' +
      'pk.eyJ1IjoiYW50b25pb2JjIiwiYSI6ImNqZWZ3cDV4dTFpOXMycWt0M2dnOGR5ZWgifQ.VckQam7DS6LDablBrZkslw';

  // OpenStreetMap
  osm = new TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 18,
    attribution: '<a href="http://openstreetmap.org">OpenStreetMap</a>'
  });
  mbg = new TileLayer(this.mbUrl, {attribution: this.mbAttr, id: 'mapbox.light'});
  mbs = new TileLayer(this.mbUrl, {attribution: this.mbAttr, id: 'mapbox.streets'});

  // Google satelite
  goog = new TileLayer('http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}', {maxZoom: 18, attribution: 'google'});

  map: LeafletMap;
  center: LatLng = new LatLng(38.843099, -9.146054);
  zoom = 12;
  fitBounds = null;

  options = {
    layers: [ this.mbs ],
    maxZoom: 25,
  };

  mapLayers = [];

  layersControl = {
    baseLayers: {
      'Open Street Map': this.osm,
      'Mapbox streets': this.mbs,
      'Mapbox gray': this.mbg,
      'Google': this.goog
    }
  };

  sidenavOpened = true;
  mousePos: LatLng = new LatLng(38.843099, -9.146054);

  defaultRailwayProperties = {
    visible: true,
    editMode: true,
    color: '#f16659',
    weight: 3,
    opacity: 1,
  };

  testBuilding: Building = {
    id: 1,
    name: 'Teste',
    elevation: 10,
    perimeter: {
      type: 'Polygon',
      coordinates: [[
        [ 38.707397, -9.161633],
        [ 38.70564, -9.1707735],
        [ 38.71564, -9.1707735]
      ]]
    },
    properties: {}
  };


  constructor(private route: ActivatedRoute,
    public router: Router,
    public projectService: ProjectService,
    private contextMenuService: ContextMenuService,
    private appService: AppService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private zone: NgZone,
    /*private ngProgress: NgProgress*/) {

    this.menuSubscription = appService.menuCommand$.subscribe(
      command => {
        if (command[0] === 'project') {
          this[command[1]]();
        }
    });
  }

  ngOnInit() {
    /*this.progressRef = this.ngProgress.ref();
    // Progress bar events (optional)
    this.progressRef.started.subscribe(() => this.onPBStarted());
    this.progressRef.completed.subscribe(() => this.onPBCompleted());*/
    console.log('project init');
    const maxRailways = 30;
    // load project
    this.route.paramMap.pipe(switchMap((params: ParamMap) => this.projectService.getProject(parseInt(params.get('id'), 10))))
      .subscribe(project => {
        this.clearLayers();
        this.dialog.closeAll();
        if (!project.properties) {
          project.properties = {};
        } else if (typeof project.properties === 'string') {
          project.properties = JSON.parse(project.properties);
        }
        if (project.properties.mapZoom) {
          this.zoom = project.properties.mapZoom;
          this.center = new LatLng(project.properties.mapCenterLat, project.properties.mapCenterLng);
        }
        this.project = project;
        this.appService.currentProject = project;
        // load railways
        project.properties.busy = 'loading';
        project.properties.loadersBusy = 1;
        project.properties.loadersTotal = 1;
        this.projectService.getProjectRailways(this.project).pipe(finalize(
          () => { project.properties.loadersBusy--; }
        )).subscribe(
          railways => {
            this.railways = [];
            if (railways.length > 0) {
              project.properties.loadersBusy += railways.length;
              project.properties.loadersTotal += railways.length;
            }
            this.projectDetails.railwayCount = railways.length;
            console.log('Railways: ' + railways.length);
            // load all railways
            for (let i = 0; i < Math.min(maxRailways, railways.length); i++) {
              this.projectService.getProjectRailway(this.project, railways[i]).pipe(
                finalize(() => project.properties.loadersBusy--)).subscribe(
                railway => {
                  this.addRailway(railway);
                },
                error => {
                  console.log('Error getting railway ' + railways[i].name + ': ' + error.statusText);
                  this.railways.push(railways[i]);
                  railways[i].railwayPoints = {type: 'FeatureCollection', features: []};
                  railways[i].error = error.statusText;
                  railways[i].properties = null;
                  this.insertLayer(railways[i], this.layers);
                }
              );
            }
          },
          error => {
            alert('Error getting railways: ' + error.statusText);
          }
        );
        // load clutters
        project.properties.loadersBusy++;
        project.properties.loadersTotal++;
        this.projectService.getProjectClutters(this.project).pipe(finalize( () => {project.properties.loadersBusy--; })).subscribe(
          clutters => {
            this.clutters = [];
            project.properties.loadersBusy += clutters.length;
            project.properties.loadersTotal += clutters.length;
            this.projectDetails.clutterCount = clutters.length;
            console.log('Clutters: ' + clutters.length);
            for (const c of clutters) {
              this.projectService.getProjectClutter(this.project, c).pipe(finalize(() => {project.properties.loadersBusy--; }))
              .subscribe(
                clutter => {
                  this.addClutter(clutter);
              },
              error => {
                console.log('Error getting clutter ' + c.name  + ':' + error.statusText);
                c.clutterTilesHref = '';
                c.error = error.message;
                this.insertLayer(c, this.layers);
              }
              );
            }
          },
          error => {
            alert('Error getting clutters:' + error.statusText);
          }
        );
        // load infrastructures
        project.properties.loadersBusy++;
        project.properties.loadersTotal++;
        this.projectService.getProjectRailwayInfrastructures(this.project).pipe(finalize(() => {project.properties.loadersBusy--; }))
        .subscribe(
        railwayis => {
          project.properties.loadersBusy += railwayis.length;
          project.properties.loadersTotal += railwayis.length;
          this.projectDetails.infrastructureCount = railwayis.length;
          console.log('Infrastructures: ' + railwayis.length);
          for (let ri = 0; ri < railwayis.length; ri++) {
            this.projectService.getRailwayInfrastructure(railwayis[ri]).pipe(finalize(() => {project.properties.loadersBusy--; }))
            .subscribe(
            infrastructure => {
              this.addInfrastructure(infrastructure);
            },
            error => {
              console.log('Error getting infrastructure ' + railwayis[ri].name + '\n' + error.message);
              railwayis[ri].error = error.error.detail;
              this.insertLayer(railwayis[ri], this.layers);
            });
          }
        },
        error => {
          console.log('Error getting railway infrastructures:' + error.statusText);
        });
        // load building groups
        project.properties.loadersBusy++;
        project.properties.loadersTotal++;
        this.projectService.getProjectBuildingGroups(this.project).pipe(finalize(() => {project.properties.loadersBusy--; }))
        .subscribe(
          bgs => {
            // this.buildingGroups = bgs;
            if (bgs.length > 0) {
              project.properties.loadersBusy += bgs.length;
              project.properties.loadersTotal += bgs.length;
            }
            this.projectDetails.buildingGroupCount = bgs.length;
            console.log('Building groups: ' + bgs.length);
            // load all building groups
            for (let i = 0; i < bgs.length; i++) {
              this.projectService.getBuildingGroup(bgs[i]).pipe(finalize(() => {project.properties.loadersBusy--; }))
              .subscribe(
                buildingGroup => {
                  this.addBuildingGroup(buildingGroup);
                },
                error => {
                  console.log('Error getting BuildingGroup ' + bgs[i].name + ': ' + error.statusText);
                }
              );
            }
          },
          error => {
            alert('Error getting railways: ' + error.statusText);
          }
        );
        // load PE
        this.projectService.getProjectPE(this.project).subscribe(pe => {
          this.projectPE = pe;
        },
        resp => {
          console.log('No project propagation environment found. ' + resp.error.detail);
        });

      },
      () => { // Error getting Project
        this.router.navigate(['home/projects']);
      });
  }

  ngOnDestroy(): void {
    this.menuSubscription.unsubscribe();
    this.appService.currentProject = null;
  }

  addRailway(railway: Railway) {
    if (!railway.properties) {
      railway.properties = { visible: true };
    } else if (typeof railway.properties === 'string') {
      railway.properties = JSON.parse(railway.properties);
    }
    if (!railway.properties.color) {
      railway.properties.color = '#f16659';
    }
    if (!railway.properties.weight) {
      railway.properties.weight = 3;
    }
    if (!railway.properties.opacity) {
      railway.properties.opacity = 1;
    }
    if (railway.railwayPoints.features.length === 1) { railway.railwayPoints.features = []; }
    this.railways.push(railway);
    this.setRailwayPointsIdx(railway);
    this.insertLayer(railway, this.layers);
  }

  addClutter(clutter: Clutter) {
    // init clutter
    if (!clutter.properties) {
      clutter.properties = { visible: false };
    } else if (typeof clutter.properties === 'string') {
      clutter.properties = JSON.parse(clutter.properties);
    }
    if (!clutter.properties.opacity) {
      clutter.properties.opacity = 1;
    }
    this.clutterClassesSort(clutter);
    this.clutters.push(clutter);
    this.insertLayer(clutter, this.layers);

    const cLayer: TileLayerH =
      new TileLayerH(this.projectService, clutter.clutterTilesHref, { maxZoom: 18, attribution: '...', pane: 'clutters' });
    this.clutterMapLayers.set(clutter, cLayer);
    if (clutter.properties.visible) { cLayer.addTo(this.map); }
  }

  addInfrastructure(infrastructure: RailwayInfrastructure) {
    if (!infrastructure.properties) {
      infrastructure.properties = { visible: true };
    } else if (typeof infrastructure.properties === 'string') {
      infrastructure.properties = JSON.parse(infrastructure.properties);
    }
    if (!('visible' in infrastructure.properties)) {
      infrastructure.properties.visible = true;
    }
    if (!('color' in infrastructure.properties)) {
      infrastructure.properties.color = '#FF4444';
    }
    this.infrastructures.push(infrastructure);
    const lGroup = new FeatureGroup();
    this.infrastructureMapLayerGroups.set(infrastructure, lGroup);
    this.insertLayer(infrastructure, this.layers);
    // Elements
    this.project.properties.loadersBusy++;
    this.project.properties.loadersTotal++;
    this.projectService.getRailwayInfrastructureElements(infrastructure)
      .pipe(finalize(() => { this.project.properties.loadersBusy--; }))
      .subscribe(
        elements => {
          /*console.log('got elements: ' + elements.length);
          project.properties.loadersBusy += elements.length;
          project.properties.loadersTotal += elements.length;
          for (let i = 0; i < elements.length; i++) {
            console.log('element ' + i + ': ' + JSON.stringify(elements[i]));
            this.projectService.getProjectRailwayInfrastructureElement(project, infrastructure, elements[i])
            .pipe(finalize(() => {project.properties.loadersBusy--; }))
            .subscribe( element => { elements[i] = element; });
          }*/
          this.infrastructureElements.set(infrastructure, elements);
          this.infrastructureElementsLayersMap.set(infrastructure, new Map<RailwayElement, Layer>());
        },
        error => {
          alert('Error getting infrastructure elements' + infrastructure.name + '\n' + error.message);
        });
    // Tunnels
    this.project.properties.loadersBusy++;
    this.project.properties.loadersTotal++;
    this.projectService.getRailwayInfrastructureTunnels(infrastructure)
      .pipe(finalize(() => { this.project.properties.loadersBusy--; }))
      .subscribe(
        tunnels => {
          this.infrastructureTunnels.set(infrastructure, tunnels);
        },
        error => {
          alert('Error getting infrastructure tunnels' + infrastructure.name + '\n' + error.message);
        });
  }

  addBuildingGroup(buildingGroup: BuildingGroup) {
    if (!buildingGroup.properties) {
      buildingGroup.properties = { visible: true };
    } else if (typeof buildingGroup.properties === 'string') {
      buildingGroup.properties = JSON.parse(buildingGroup.properties);
    }
    if (!buildingGroup.properties.color) {
      buildingGroup.properties.color = '#f16659';
    }
    if (!buildingGroup.properties.weight) {
      buildingGroup.properties.weight = 3;
    }
    this.buildingGroups.push(buildingGroup);
    const lGroup = new Deflate(); // FeatureGroup();
    this.buildingsMapLayerGroups.set(buildingGroup, lGroup);
    this.insertLayer(buildingGroup, this.layers);
    this.project.properties.loadersBusy++;
    this.project.properties.loadersTotal++;
    this.projectService.getBuildingGroupBuildings(buildingGroup).pipe(finalize(() => {this.project.properties.loadersBusy--; }))
    .subscribe(
      buildings => {
        if (buildings.length < 1000) {
          this.buildings.set(buildingGroup, buildings);
        } else {
          const clusterBuildings = [];
          this.buildings.set(buildingGroup, clusterBuildings);
          console.log(`bGroup ${buildingGroup.name}: ${buildings.length} buildings`);
          // this.buildings.set(buildingGroup, buildings.slice(0, 1000));
          const newPoints = [];
          for (const building of buildings) {
            const c = getCentroid(building.perimeter.coordinates[0]);
            if (!isFinite(c[0]) || !isFinite(c[1])) {
              console.log('invalid building: ' + building.name + '; centroid: ' + c);
              console.log(JSON.stringify(building.perimeter));
            } else {
              // newPoints.push({x: c[0], y: c[1]});
              newPoints.push([c[0], c[1]]);
            }
          }
          // newPoints = newPoints.splice(100);
          /*const res = dobbyscan(newPoints, 1);
          console.log('noise: ' + res.noise.length);
          console.log('clusters: ' + res.clusters.length);*/
          const clusters = dobbyscan(newPoints, .05);
          console.log('clusters: ' + clusters.length);
          for (const cluster of clusters) {
            const clusterPoints = [];
            for (const coord of cluster) {
              clusterPoints.push({x: coord[0], y: coord[1]});
            }
            const gwPoints = graham(clusterPoints);
            const newPoints2 = [];
            for (const p of gwPoints) {
              newPoints2.push([p.x, p.y, 0]);
            }
            if (newPoints2.length > 3) {
              clusterBuildings.push({
                name: 'cluster of ' + cluster.length + ' buildings' ,
                elevation: 0,
                id: 0,
                properties: {},
                perimeter: {
                  type: 'Polygon',
                  coordinates: [newPoints2]
                },
              });
            }
          }
        }

        for (const building of buildings) {
          if (building.properties && typeof building.properties === 'string') {
            building.properties = JSON.parse(building.properties);
          }
          // building.perimeter.coordinates[0].pop();
        }
      },
      error => {
        console.log('Error getting BuildingGroup buildings "' + buildingGroup.name + '": ' + error.error.detail);
      }
    );
  }

  layerType(layer: BaseLayer): string {
     return (this.isRailway(layer) ? 'railway' :
            this.isClutter(layer) ? 'clutter' :
            this.isBuildingGroup(layer) ? 'buildingGroup' :
            this.isInfrastructure(layer) ? 'infrastructure' :
            'invalidLayerType');
  }

  layersPBCompleted(ev) {
    if (ev.value === 100) {
      console.log('All Layers loaded.');
      delete this.project.properties.loadersTotal;
      delete this.project.properties.loadersBusy;
      delete this.project.properties.busy;
      this.saveProjectProperties(true);
    }
  }

  saveProjectProperties(calcLayerOrder: boolean) {
    if (calcLayerOrder) {
      this.project.properties.layerOrder = []; // [type, id] array
      this.layers.forEach((layer) => {
        this.project.properties.layerOrder.push([this.layerType(layer), layer.id]);
      });
    }
    this.projectService.updateProject(this.project).subscribe();
  }

  clearLayers() {
    // clear all layers
    this.layers = [];
    this.railways = [];
    this.railwayPolylines = new Map<Railway, Polyline>();
    this.peLayers = [];
    this.clutters = [];
    this.clutterClasses.clear();
    this.clutterMapLayers.forEach(layer => layer.removeFrom(this.map));
    this.clutterMapLayers = new Map<Clutter, TileLayer>();
    this.infrastructureElements = new Map<RailwayInfrastructure, RailwayElement[]>();
    // this.infrastructureElementsLayersMap.forEach(layer => layer.removeFrom(this.map));
    this.infrastructureElementsLayersMap = new Map<RailwayInfrastructure, Map<RailwayElement, Layer>>();
    this.infrastructureMapLayerGroups.forEach(layer => layer.removeFrom(this.map));
    this.infrastructureMapLayerGroups = new Map<RailwayInfrastructure, FeatureGroup>();
    this.buildingGroups = [];
    this.buildings = new Map<BuildingGroup, Building[]>();
    this.buildingsMapLayerGroups = new Map<BuildingGroup, FeatureGroup>();
  }

  newProject() {
    const newProject = new Project();
      newProject.coordinateSystem = 4326;
      newProject.name = 'Untitled project';
      this.projectService.addProject(newProject).subscribe(
        resp => {
          if (resp.status >= 200 && resp.status < 300) {
            this.projectService.getProject(resp.headers.get('Location')).subscribe( project => {
                if (!project.properties) {
                    project.properties = {};
                } else if (typeof project.properties === 'string') {
                    project.properties = JSON.parse(project.properties);
                }
                this.project = project;
                this.appService.currentProject = project;
                setTimeout(() => this.appService.currentProject = this.project, 0);
                this.clearLayers();
                this.snackBar.open('Project created.', '', { duration: 2000 });
            });
          } else { alert('Error creating project: ' + inspect(resp)); }
        }
      );
  }

  openProject() {
    this.dialog.open(ProjectlistdialogComponent, {
      closeOnNavigation: true,
    });
  }

  editProject() {
    const dialogRef = this.dialog.open(ProjectdetailsdialogComponent, {
      data: [this.project, this.projectDetails]
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        this.saveProject();
      }
    });
  }


  saveProject() {
    // this.project.properties = JSON.stringify(this.project.properties);
    this.projectService.updateProject(this.project).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          this.snackBar.open('Project saved.', '', { duration: 2000 });
        } else {
          alert('Error patching project:' + resp.statusText); }
      }
    );
  }

  deleteProject() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: ['Confirm', 'Are you sure?']
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        if (this.project.id !== 0) {
          this.projectService.delProject(this.project).subscribe(
          resp => {
            if (resp.status >= 200 && resp.status < 300) {
              this.snackBar.open('Project deleted.', '', {duration: 2000});
              this.router.navigate(['home/projects']);
            } else {
              this.snackBar.open('Error deleting project:' + resp.statusText, '', {duration: 2000});
            }
          }
          );
        }
      }
    });
  }

  newPE() {
    const newPE = new PropagationEnvironment();
    const mapBounds = this.map.getBounds();
    newPE.boundingBox = {
      bbox: [mapBounds.getNorthEast().lng, mapBounds.getNorthEast().lat, mapBounds.getSouthWest().lng, mapBounds.getSouthWest().lat]
    };

    const dialogRef = this.dialog.open(PEDetailsDialogComponent, {
      data: [newPE, {}]
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        this.projectService.addProjectPE(this.project, newPE).subscribe(
          resp => {
            if (resp.status >= 200 && resp.status < 300) { // Load from location header (invalid location, /id)
              this.projectService.getProjectPE(/*resp.headers.get('Location')*/this.project).subscribe(pe => {
                this.snackBar.open('Project PE created.', '', { duration: 2000 });
                this.projectPE = pe;
              },
              resp2 => {
                alert('error getting newly created PE:' + resp2.error.detail);
              });
            } else {
              alert('Error creating PE:' + resp.statusText);
            }
          },
          error => {
            alert(`Error creating PE: ${error.error}`);
          }
    );
      } else {
        this.snackBar.open('Project PE creation canceled.', '', { duration: 2000 });
      }
    });
  }

  addLayer() {
    const dialogRef = this.dialog.open(LayerlistdialogComponent, {
      closeOnNavigation: true,
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.project.properties.busy = 'loading';
        this.project.properties.loadersBusy = 1;
        this.project.properties.loadersTotal = 1;
        const layer = <BaseLayer>result;
        if ('railwayPoints' in layer) {
          const railway = <Railway> layer;
          this.projectService.addPublishedRailway(this.project, railway).pipe(finalize(() => {this.project.properties.loadersBusy--; }))
          .subscribe(
            resp => {
              if (resp.status >= 200 && resp.status < 300) {
                this.projectService.getProjectRailway(resp.headers.get('Location')).subscribe(
                  newRailway => {
                    this.addRailway(newRailway);
                    this.snackBar.open('Railway added.', '', { duration: 2000 });
                    this.saveProjectProperties(true);
                  });
              } else {
                alert('Error adding published railway: ' + resp.statusText);
              }
            },
            err => {
              // err.error = response body
              const error = JSON.parse(err.error);
              alert('Error adding published railway: ' + error.detail);
            }
          );
        } else if ('clutterTilesHref' in layer) {
          const clutter = <Clutter> layer;
          this.projectService.addPublishedClutter(this.project, clutter).pipe(finalize(() => {this.project.properties.loadersBusy--; }))
          .subscribe(
            resp => {
              if (resp.status >= 200 && resp.status < 300) {
                this.projectService.getProjectClutter(resp.headers.get('Location')).subscribe(
                  newClutter => {
                    this.addClutter(newClutter);
                    this.snackBar.open('Clutter added.', '', { duration: 2000 });
                    this.saveProjectProperties(true);
                  }
                );
              } else {
                alert('Error adding published clutter: ' + resp.statusText);
              }
            },
            err => {
              // err.error = response body
              const error = JSON.parse(err.error);
              alert('Error adding published clutter: ' + error.detail);
            }
          );
        } else if ('buildingsHref' in layer) {
          const buildingGroup = <BuildingGroup> layer;
          this.projectService.addPublishedBuildingGroup(this.project, buildingGroup)
          .pipe(finalize(() => {this.project.properties.loadersBusy--; }))
          .subscribe(
            resp => {
              if (resp.status >= 200 && resp.status < 300) {
                this.projectService.getProjectBuildingGroup(resp.headers.get('Location')).subscribe(
                  newBG => {
                    this.addBuildingGroup(newBG);
                    this.snackBar.open('Building Group added.', '', { duration: 2000 });
                    this.saveProjectProperties(true);
                  }
                );
              } else {
                alert('Error adding published buildingGroup: ' + resp.statusText);
              }
            },
            err => {
              // err.error = response body
              const error = JSON.parse(err.error);
              alert('Error adding published clutter: ' + error.detail);
            }
          );
        } else if ('elementsHref' in layer) {
          const infrastructure = <RailwayInfrastructure> layer;
          this.projectService.addPublishedRailwayInfrastructure(this.project, infrastructure)
          .pipe(finalize(() => {this.project.properties.loadersBusy--; }))
          .subscribe(
            resp => {
              if (resp.status >= 200 && resp.status < 300) {
                this.projectService.getProjectRailwayInfrastructure(resp.headers.get('Location')).subscribe(
                  newInf => {
                    this.addInfrastructure(newInf);
                    this.snackBar.open('Railway Infrastructure added.', '', { duration: 2000 });
                    this.saveProjectProperties(true);
                  }
                );
              } else {
                alert('Error adding published infrastructure: ' + resp.statusText);
              }
            },
            err => {
              // err.error = response body
              const error = JSON.parse(err.error);
              alert('Error adding published infrastructure: ' + error.detail);
            }
          );
        }
      }
    });
  }


  newRailway() {
    const newRailway = new Railway();
    newRailway.id = 0;
    newRailway.coordinateSystem = 4326;
    newRailway.state = 'Unlocked';
    newRailway.properties = this.defaultRailwayProperties;
    newRailway.railwayPoints = {
      'type': 'FeatureCollection',
      'features': []
    };
    // newRailway.properties = {visible: true, editMode: true};
    const dialogRef = this.dialog.open(LayerpropertiesdialogComponent, {
      data: [this.project, newRailway]
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        this.railways.push(newRailway);
        this.insertLayer(newRailway, this.layers);
        this.saveProjectProperties(true);
      } else {
//        this.snackBar.open('Railway creation canceled.', '', { duration: 2000 });
      }
    });
  }

  saveRailway(railway: Railway) {
    if (railway.railwayPoints.features.length < 2) { return; }
    const tmp2 = railway.properties;
    railway.properties = JSON.stringify(tmp2);
    if (railway.id === 0) {
      this.projectService.addProjectRailway(this.project, railway).subscribe(
       resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.projectService.getProjectRailway(resp.headers.get('Location')).subscribe(
             newRailway => {
               newRailway.properties = JSON.parse(newRailway.properties);
               this.railways[this.railways.indexOf(railway)] = newRailway;
               this.layers[this.layers.indexOf(railway)] = newRailway;
               this.snackBar.open('Railway created.', '', {
                duration: 2000,
               });
            });
         } else { alert('Error patching railway:' + resp.statusText); }
       },
        error => {
          // error.error = response body
          alert('Error saving railway:' + error.error.detail);
        }
      );
    } else {
      this.projectService.updateProjectRailway(this.project, railway).subscribe();
    }
    railway.properties = tmp2;
  }

  cloneLayer(layer: Railway | Clutter | RailwayInfrastructure | BuildingGroup, newName?: string) {
    newName = newName || ('copy of ' + layer.name);
    if ('railwayPoints' in layer) {
      const railway = <Railway> layer;
      this.projectService.cloneProjectRailway(this.project, railway, newName).subscribe(
       resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.projectService.getProjectRailway(resp.headers.get('Location')).subscribe(
             newRailway => {
               newRailway.properties = JSON.parse(newRailway.properties);
               this.railways.push(newRailway);
               this.layers.push(newRailway);
               this.saveProjectProperties(true);
               this.snackBar.open('Railway cloned.', '', { duration: 2000 });
            });
         } else {
           alert('Error cloning railway: ' + resp.statusText);
          }
       },
        err => {
          // err.error = response body
          const error = JSON.parse(err.error);
          alert('Error cloning railway: ' + error.detail);
        }
      );
    } else if ('buildingsHref' in layer) {
      const buildingGroup = <BuildingGroup> layer;
      this.projectService.cloneProjectBuildingGroup(this.project, buildingGroup, newName).subscribe(
       resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.projectService.getProjectBuildingGroup(resp.headers.get('Location')).subscribe(
             newBg => {
               this.addBuildingGroup(newBg);
               this.saveProjectProperties(true);
              this.snackBar.open('Building Group cloned.', '', { duration: 2000 });
            });
         } else {
           alert('Error cloning Building group: ' + resp.statusText);
          }
       },
        err => {
          // err.error = response body
          const error = JSON.parse(err.error);
          alert('Error cloning Building group: ' + error.detail);
        }
      );
    } else if ('clutterTilesHref' in layer) {
      const clutter = <Clutter> layer;
      this.projectService.cloneProjectClutter(this.project, clutter, newName).subscribe(
       resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.projectService.getProjectClutter(resp.headers.get('Location')).subscribe(
             newClutter => {
               newClutter.properties = JSON.parse(newClutter.properties);
               this.clutters.push(newClutter);
               this.insertLayer(newClutter, this.layers);
               const cLayer: TileLayerH =
                 new TileLayerH(this.projectService, newClutter.clutterTilesHref, {maxZoom: 18, attribution: '...', pane: 'clutters'} );
               this.clutterMapLayers.set(newClutter, cLayer);
               if (newClutter.properties.visible) { cLayer.addTo(this.map); }
               this.saveProjectProperties(true);
               this.snackBar.open('Clutter cloned.', '', { duration: 2000 });
            });
         } else {
           alert('Error cloning Clutter group: ' + resp.statusText);
          }
       },
        err => {
          // err.error = response body
          const error = JSON.parse(err.error);
          alert('Error cloning Clutter: ' + error.detail);
        }
      );
    } else if ('railwayElementsHref' in layer) {
      const infrastructure = <RailwayInfrastructure> layer;
      this.projectService.cloneProjectRailwayInfrastructure(this.project, infrastructure, newName).subscribe(
       resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.projectService.getProjectRailwayInfrastructure(resp.headers.get('Location')).subscribe(
             newInfrastructure => {
               newInfrastructure.properties = JSON.parse(newInfrastructure.properties);
              this.infrastructures.push(infrastructure);
              const lGroup = new FeatureGroup();
              this.infrastructureMapLayerGroups.set(newInfrastructure, lGroup);
              // lGroup.addTo(this.map);
              this.insertLayer(newInfrastructure, this.layers);
              this.saveProjectProperties(true);
              this.projectService.getRailwayInfrastructureElements(newInfrastructure).subscribe(
                elements => {
                  /* for (let i = 0; i < elements.length; i++) {
                    this.projectService.getProjectRailwayInfrastructureElement(project, infrastructure, elements[i])
                    .subscribe( element => { elements[i] = element; });
                  } */
                  this.infrastructureElements.set(newInfrastructure, elements);
                  this.infrastructureElementsLayersMap.set(newInfrastructure, new Map<RailwayElement, Layer>());
                  newInfrastructure.properties.elementsChanged = true;
              },
              error => {
                alert('Error getting infrastructure elements' + newInfrastructure.name + '\n' + error.message);
              });
               this.snackBar.open('Infrastructure cloned.', '', { duration: 2000 });
            });
         } else {
           alert('Error cloning Infrastructure group: ' + resp.statusText);
          }
       },
        err => {
          // err.error = response body
          const error = JSON.parse(err.error);
          alert('Error cloning Infrastructure: ' + error.detail);
        }
      );
    }
  }

  /*cloneRailway(railway: Railway, newName?: string) {
    newName = newName || ('copy of ' + railway.name);
    this.projectService.cloneProjectRailway(this.project, railway, newName).subscribe(
       resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.projectService.getProjectRailway(resp.headers.get('Location')).subscribe(
             newRailway => {
               newRailway.properties = JSON.parse(newRailway.properties);
               this.railways.push(newRailway);
               this.layers.push(newRailway);
               this.snackBar.open('Railway cloned.', '', { duration: 2000 });
            });
         } else {
           alert('Error cloning railway: ' + resp.statusText);
          }
       },
        err => {
          // err.error = response body
          const error = JSON.parse(err.error);
          alert('Error cloning railway: ' + error.detail);
        }
      );
  }*/

  saveRailwayProperties(railway: Railway, success?) {
    if (railway.id === 0) { return; }
    this.projectService.updateProjectRailwayProperties(this.project, railway).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          if (success) {success(); }
        } else { alert('Error patching railway properties:' + resp.statusText); }
      },
      err => {
        // this.snackBar.open("Error saving railway properties: " + JSON.stringify(err));
        const error = JSON.parse(err.error);
        alert('Error saving railway properties: ' + error.detail);
      }
    );
  }

  setRailwayPointsIdx(railway: Railway) {
    for (let k = 0; k < railway.railwayPoints.features.length; k++) {
      railway.railwayPoints.features[k].properties.idx = k;
    }
  }

  newInfrastructure() {
    const newInfrastructure = new RailwayInfrastructure();
    newInfrastructure.id = 0;
    newInfrastructure.coordinateSystem = 4326;
    newInfrastructure.state = 'Unlocked';
    newInfrastructure.properties = this.defaultRailwayProperties;
    const dialogRef = this.dialog.open(LayerpropertiesdialogComponent, {
      data: [this.project, newInfrastructure]
    });
    const _comp = this;
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        newInfrastructure.properties = JSON.stringify(newInfrastructure.properties);
        this.projectService.addProjectRailwayInfrastructure(_comp.project, newInfrastructure).subscribe(
          resp => {
            if (resp.status >= 200 && resp.status < 300) {
              this.projectService.getRailwayInfrastructure(resp.headers.get('Location')).subscribe( infrastructure => {
                _comp.layers.push(infrastructure);
                _comp.infrastructures.push(infrastructure);
                const lGroup = new FeatureGroup();
                _comp.infrastructureMapLayerGroups.set(infrastructure, lGroup);
                _comp.infrastructureElements.set(infrastructure, []);
                _comp.infrastructureElementsLayersMap.set(infrastructure, new Map<RailwayElement, Layer>());
                _comp.saveProjectProperties(true);
                this.snackBar.open('Infrastructure created.', '', { duration: 2000 });
              });
            } else {
              alert('Error creating infrastructure:' + resp.statusText);
            }
          }
        );
      } else {
        this.snackBar.open('Railway infrastructure creation canceled.', '', { duration: 2000 });
      }
    });
  }

  saveInfrastructure(infrastructure: RailwayInfrastructure, success?) {
    const tmp2 = infrastructure.properties;
    infrastructure.properties = JSON.stringify(tmp2);
    this.projectService.updateProjectRailwayInfrastructure(this.project, infrastructure).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          infrastructure.properties = tmp2;
          this.snackBar.open('Infrastructure saved.', '', {
          duration: 2000,
          });
          if (success) {success(); }
        } else {
          infrastructure.properties = tmp2;
          alert('Error patching infrastructure:' + resp.statusText);
        }
      }
    );
    // console.log('saving elements:' + this.infrastructureElements.get(infrastructure).length);
    // this.saveInfrastructureElements(infrastructure);
  }

  saveInfrastructureProperties(infrastructure: RailwayInfrastructure) {
    this.projectService.updateProjectRailwayInfrastructureProperties(this.project, infrastructure).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
        } else { alert('Error saving infrastructure properties:' + resp.statusText); }
      }
    );
  }

  saveInfrastructureElement(infrastructure: RailwayInfrastructure, element: RailwayElement, success?) {
    const tmp2 = element.properties;
    element.properties = JSON.stringify(tmp2);
    delete element.properties;
    if (element.id === 0) {
      this.projectService.addProjectRailwayInfrastructureElement(this.project, infrastructure, element).subscribe(
        resp => {
          if (resp.status >= 200 && resp.status < 300) {
            this.snackBar.open('Element created.', '', {
              duration: 2000,
            });
            if (success) { success(); }
          } else { alert('Error creating element:' + resp.statusText); }
        }
      );
    } else {
      this.projectService.updateProjectRailwayInfrastructureElement(this.project, infrastructure, element).subscribe(
        resp => {
          if (resp.status >= 200 && resp.status < 300) {
            this.snackBar.open('Element saved.', '', {
              duration: 2000,
            });
            if (success) { success(); }
          } else { alert('Error patching element:' + resp.statusText); }
        }
      );
    }
    element.properties = tmp2;
  }

  saveClutter(clutter: Clutter, success?) {
    const tmp2 = clutter.properties;
    clutter.properties = JSON.stringify(tmp2);
    this.projectService.updateProjectClutter(this.project, clutter).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          this.snackBar.open('Clutter saved.', '', {duration: 2000});
          if (success) {success(); }
        } else { alert('Error patching clutter:' + resp.statusText); }
      }
    );
    clutter.properties = tmp2;
  }

  saveClutterClasses(clutter: Clutter, success?) {
    this.projectService.updateProjectClutterClasses(this.project, clutter).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          this.snackBar.open('Clutter classes saved.', '', {duration: 2000});
          if (success) {success(); }
        } else { alert('Error patching clutter classes:' + resp.statusText); }
      }
    );
  }

  saveClutterProperties(clutter: Clutter, success?) {
    this.projectService.updateProjectClutterProperties(this.project, clutter).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          if (success) {success(); }
        } else { alert('Error patching clutter properties:' + resp.statusText); }
      }
    );
  }

  onMapReady(map: LeafletMap) {
    console.log('Map init');
    this.map = map;
    map.createPane('clutters');
    map.getPane('clutters').style.zIndex = '300';
    this.map.on('contextmenu', this.onMapContextMenu, this);
    this.map.on('mousemove', this.onMapMouseMove, this);
    this.map.on('zoomend', this.onMapMove, this);
    this.map.on('dragend', this.onMapMove, this);
    this.map.on('moveend', this.onMapMove, this);
  }

  onMapContextMenu(ev) {
    ev.originalEvent.preventDefault();
  }

  onMapMove() {
    this.project.properties.mapZoom = this.map.getZoom();
    const center = this.map.getCenter();
    this.project.properties.mapCenterLat = center.lat;
    this.project.properties.mapCenterLng = center.lng;
    this.saveProjectProperties(false);
  }

  onMapMouseMove(e) {
    this.zone.run( () => {
      this.mousePos = e.latlng;
    });
  }

  fitBoundsLayer(layer: BaseLayer) {
    if (this.isRailway(layer)) {

      this.map.fitBounds(this.railwayPolylines.get(<Railway>layer).getBounds());
    }
    if (this.isInfrastructure(layer)) {
      layer.properties.approach = true;
      /*const infrastructure = <RailwayInfrastructure>layer;
      const group = this.infrastructureMapLayerGroups.get(infrastructure);
      const count = group.getLayers().length;
      if (count === 1) {
        const element: RailwayElement = this.infrastructureElements.get(infrastructure)[0];
        this.center = new LatLng(element.location.geometry.coordinates[1], element.location.geometry.coordinates[0]);
      } else {
        this.fitBounds = group.getBounds();
      }
      */
    }
    if (this.isBuildingGroup(layer)) {
      const group = this.buildingsMapLayerGroups.get(<BuildingGroup>layer);
      this.map.fitBounds(group.getBounds());
    }
    if (this.isClutter(layer)) {
      const cl = <Clutter>layer;
      this.map.fitBounds([[cl.boundingBox.bbox[1], cl.boundingBox.bbox[0]],
        [cl.boundingBox.bbox[3], cl.boundingBox.bbox[2]]]);
      }
  }

  layerProperties(layer: BaseLayer) {
    const dialogRef = this.dialog.open(LayerpropertiesdialogComponent, {
      data: this.isInfrastructure(layer) ?
        [this.project, layer, this.railways, this.infrastructureElements.get(<RailwayInfrastructure>layer)] :
      this.isBuildingGroup(layer) ?
        [this.project, layer, this.buildings.get(<BuildingGroup>layer)] :
      //   this.isClutter(layer) ?
      // [this.project, layer, this.railways, null, this.clutterClasses.get(<Clutter>layer)] :
      [this.project, layer],
      disableClose: true
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        if (this.isClutter(layer)) {
          this.saveClutterProperties(<Clutter>layer);
          this.saveClutterClasses(<Clutter>layer, () => {
            if (layer.properties.visible) {
              const mLayer = this.clutterMapLayers.get(<Clutter>layer);
              mLayer.removeFrom(this.map);
              mLayer.addTo(this.map);
              mLayer.setOpacity(layer.properties.opacity);
            }
          });

        }
        if (this.isRailway(layer)) {
          const obs = layer.state === 'Edition' ?
            this.projectService.updateProjectRailwayProperties(this.project, <Railway>layer) :
            this.projectService.updateProjectRailway(this.project, <Railway>layer);
          obs.subscribe(
            resp => {
              if (resp.status >= 200 && resp.status < 300) {
              } else { alert('Error saving railway' + layer.state === 'Edition' ? '' : ' properties:' + resp.statusText); }
           },
           err => {
              const error = JSON.parse(err.error);
              this.snackBar.open(`Error saving railway${layer.state === 'Edition' ? '' : ' properties'}: ${error.detail}`,
              '', {duration: 2000});
           });
        }
        if (this.isInfrastructure(layer)) {
          const obs = layer.state === 'Edition' ?
            this.projectService.updateProjectRailwayInfrastructureProperties(this.project, <RailwayInfrastructure>layer) :
            this.projectService.updateProjectRailwayInfrastructure(this.project, <RailwayInfrastructure>layer);
          obs.subscribe(
            resp => {
              if (resp.status >= 200 && resp.status < 300) {
                layer.properties.elementsChanged = true;
              } else { alert('Error saving infrastructure' + layer.state === 'Edition' ? '' : ' properties:' + resp.statusText); }
           });
        }
        if (this.isBuildingGroup(layer)) {
          const obs = layer.state === 'Edition' ?
            this.projectService.updateProjectBuildingGroupProperties(this.project, <BuildingGroup>layer) :
            this.projectService.updateProjectBuildingGroup(this.project, <BuildingGroup>layer);
          obs.subscribe(
            resp => {
              // layer.properties = JSON.parse(layer.properties);
              if (resp.status >= 200 && resp.status < 300) {
                console.log('buildingGroup saved');
              } else { alert('Error saving building group' + layer.state === 'Edition' ? '' : ' properties:' + resp.statusText); }
           });
        }
      }
    });
  }

  clutterClassesSort(clutter: Clutter) {
    clutter.classes = clutter.classes.sort((c1, c2) => c1.pixelValue - c2.pixelValue);
  }

  isLayerVisible(layer: BaseLayer): boolean {
    if (!('properties' in layer)) { return false; }
    if (layer.properties == null) { return false; }
    return (layer.properties.visible);
  }

  isLayerInvisible(layer: BaseLayer): boolean {
    if (!('properties' in layer) || layer.properties == null) { return false; }
    return (!layer.properties.visible);
  }

  isLayerEditable = (layer: BaseLayer): boolean => {
    if (!('properties' in layer) || layer.properties == null) { return false; }
    return /*layer.state == 'Unlocked' &&*/ !('editMode' in layer.properties) && !('clutterTilesHref' in layer);
  }

  isLayerUnlockable = (layer: BaseLayer): boolean => {
    if (!('properties' in layer)) { return false; }
    return (layer.state === 'Locked' || layer.state === 'Published') && !('editMode' in layer.properties);
  }

  isLayerLockable = (layer: BaseLayer): boolean => {
    if (!('properties' in layer)) { return false; }
    return (layer.state === 'Unlocked') && !('editMode' in layer.properties);
  }

  isLayerLocked = (layer: BaseLayer): boolean => {
    if (!('properties' in layer)) { return false; }
    return (layer.state === 'Locked' && !('editMode' in layer.properties));
  }

  isLayerinEditMode = (layer: BaseLayer): boolean => {
    if (!('properties' in layer) || layer.properties == null) { return false; }
    return 'editMode' in layer.properties;
  }

  isRemoveLayerPossible = (layer: BaseLayer): boolean => {
    if (!('properties' in layer) || layer.properties == null) { return false; }
    const inScenario = false;
    return !('editMode' in layer.properties) && !inScenario;
  }

  isDuplicateLayerPossible = (layer: BaseLayer): boolean => {
    if (!('properties' in layer)) { return false; }
    return true; // layer.state !== 'Unlocked';
  }

  isApproachLayerPossible = (layer: BaseLayer): boolean => {
    if (!('properties' in layer) || layer.properties == null) { return false; }
    if (this.isInfrastructure(layer)) {
      return layer.properties.visible && this.infrastructureElements.get(<RailwayInfrastructure>layer).length > 0;
    }
    if (this.isRailway(layer)) {
      return layer.properties.visible && (<Railway> layer).railwayPoints.features.length > 1;
    }
    return (layer.properties.visible);
  }
  isAddElementPossible = (layer: BaseLayer): boolean => {
    if (!('properties' in layer)) { return false; }
    return this.isInfrastructure(layer) && 'editMode' in layer.properties;
  }

  isLockLayerPossible = (layer: BaseLayer): boolean => {
    if (!('properties' in layer)) { return false; }
    return layer.state === 'Unlocked';
  }

  isPublishLayerPossible = (layer: BaseLayer): boolean => {
    if (!('properties' in layer)) { return false; }
    return layer.state !== 'Published';
  }

  isAddToPEPossible = (layer: BaseLayer): boolean => {
    if (!('properties' in layer)) { return false; }
    return true;
  }

  showLayer(layer: BaseLayer) {
    layer.properties.visible = true;
    if (this.isClutter(layer)) {
        this.clutterMapLayers.get(<Clutter>layer).addTo(this.map);
        this.saveClutterProperties(<Clutter>layer);
    }
    if (this.isInfrastructure(layer)) {
        this.infrastructureMapLayerGroups.get(<RailwayInfrastructure>layer).addTo(this.map);
        this.saveInfrastructureProperties(<RailwayInfrastructure>layer);
    }
    if (this.isRailway(layer)) {
        // this.infrastructureMapLayerGroups.get(<RailwayInfrastructure>layer).removeFrom(this.map);
        this.saveRailwayProperties(<Railway>layer);
    }
  }

  hideLayer(layer: BaseLayer) {
    layer.properties.visible = false;
    if (this.isClutter(layer)) {
        this.clutterMapLayers.get(<Clutter>layer).removeFrom(this.map);
        this.saveClutterProperties(<Clutter>layer);
    }
    if (this.isInfrastructure(layer)) {
        this.infrastructureMapLayerGroups.get(<RailwayInfrastructure>layer).removeFrom(this.map);
        this.saveInfrastructureProperties(<RailwayInfrastructure>layer);
    }
    if (this.isRailway(layer)) {
        // this.infrastructureMapLayerGroups.get(<RailwayInfrastructure>layer).removeFrom(this.map);
        this.saveRailwayProperties(<Railway>layer);
    }
    if (this.isBuildingGroup(layer)) {
        // this.infrastructureMapLayerGroups.get(<RailwayInfrastructure>layer).removeFrom(this.map);
        this.saveBuildingGroupProperties(<BuildingGroup>layer);
    }
  }

  removeLayer(layer: BaseLayer) {
    if (this.isRailway(layer)) {
      const railway = <Railway> layer;
      this.projectService.delProjectRailway(this.project, railway).subscribe( resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.snackBar.open('Railway removed.', '', {duration: 2000});
           this.layers.splice(this.layers.indexOf(railway), 1);
           this.railways.splice(this.railways.indexOf(railway), 1);
           this.saveProjectProperties(true);
         } else {
            this.snackBar.open('Error removing railway:' + resp.statusText, '', {duration: 2000});
         }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error removing Railway: ' + error.detail);
      });
    } else if (this.isClutter(layer)) {
      const clutter = <Clutter> layer;
      this.projectService.delProjectClutter(this.project, clutter).subscribe( resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.snackBar.open('Clutter removed.', '', {duration: 2000});
           this.layers.splice(this.layers.indexOf(clutter), 1);
           this.clutters.splice(this.clutters.indexOf(clutter), 1);
           if (clutter.properties.visible) { this.clutterMapLayers.get(clutter).removeFrom(this.map); }
           this.clutterMapLayers.delete(clutter);
           this.saveProjectProperties(true);
         } else {
            this.snackBar.open('Error removing clutter:' + resp.statusText, '', {duration: 2000});
         }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error removing Clutter: ' + error.detail);
      });
    } else if (this.isBuildingGroup(layer)) {
      const buildingGroup = <BuildingGroup> layer;
      this.projectService.delProjectBuildingGroup(this.project, buildingGroup).subscribe( resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.snackBar.open('Building group removed.', '', {duration: 2000});
           this.layers.splice(this.layers.indexOf(buildingGroup), 1);
           this.buildingGroups.splice(this.buildingGroups.indexOf(buildingGroup), 1);
           this.buildings.delete(buildingGroup);
           this.buildingsMapLayerGroups.delete(buildingGroup);
           this.saveProjectProperties(true);
         } else {
            this.snackBar.open('Error removing Building group:' + resp.statusText, '', {duration: 2000});
         }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error removing Building group: ' + error.detail);
      });
    } else if (this.isInfrastructure(layer)) {
      const infrastructure = <RailwayInfrastructure> layer;
      this.projectService.delProjectInfrastructure(this.project, infrastructure).subscribe( resp => {
         if (resp.status >= 200 && resp.status < 300) {
           this.snackBar.open('Infrastructure removed.', '', {duration: 2000});
           this.layers.splice(this.layers.indexOf(infrastructure), 1);
           this.infrastructures.splice(this.infrastructures.indexOf(infrastructure), 1);
           this.infrastructureElements.delete(infrastructure);
            this.infrastructureElementsLayersMap.delete(infrastructure);
            this.infrastructureMapLayerGroups.delete(infrastructure);
            this.saveProjectProperties(true);
         } else {
            this.snackBar.open('Error removing Infrastructure:' + resp.statusText, '', {duration: 2000});
         }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error removing Infrastructure: ' + error.detail);
      });
    }
  }

  lockLayer(layer: BaseLayer) {
    if (this.isRailway(layer)) {
      const railway = <Railway>layer;
      this.projectService.lockProjectRailway(this.project, railway).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          railway.state = 'Locked';
          this.snackBar.open('Railway locked.', '', {duration: 2000});
        } else {
          alert('Error locking layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error locking Railway: ' + error.detail);
      });
    } else if (this.isClutter(layer)) {
      const clutter = <Clutter>layer;
      this.projectService.lockProjectClutter(this.project, clutter).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          clutter.state = 'Locked';
          this.snackBar.open('Clutter locked.', '', {duration: 2000});
        } else {
          alert('Error locking layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error locking Clutter: ' + error.detail);
      });
    } else if (this.isInfrastructure(layer)) {
      const infrastructure = <RailwayInfrastructure>layer;
      this.projectService.lockProjectInfrastructure(this.project, infrastructure).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          infrastructure.state = 'Locked';
          this.snackBar.open('Infrastructure locked.', '', {duration: 2000});
          } else {
          alert('Error locking layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error locking Infrastructure: ' + error.detail);
      });
    } else if (this.isBuildingGroup(layer)) {
      const buildingGroup = <BuildingGroup>layer;
      this.projectService.lockProjectBuildingGroup(this.project, buildingGroup).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          buildingGroup.state = 'Locked';
          this.snackBar.open('Building group locked.', '', {duration: 2000});
          } else {
            alert('Error locking layer: ' + res.statusText);
          }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error locking Building group: ' + error.detail);
      });
    }
  }

  unlockLayer(layer: BaseLayer) {
    if (this.isRailway(layer)) {
      const railway = <Railway>layer;
      this.projectService.unlockProjectRailway(this.project, railway).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          railway.state = 'Unlocked';
          this.snackBar.open('Railway unlocked.', '', {duration: 2000});
        } else {
          alert('Error unlocking layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error unlocking Railway: ' + error.detail);
      });
    } else if (this.isClutter(layer)) {
      const clutter = <Clutter>layer;
      this.projectService.unlockProjectClutter(this.project, clutter).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          clutter.state = 'Unlocked';
          this.snackBar.open('Clutter unlocked.', '', {duration: 2000});
        } else {
          alert('Error unlocking layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error unlocking Clutter: ' + error.detail);
      });
    } else if (this.isInfrastructure(layer)) {
      const infrastructure = <RailwayInfrastructure>layer;
      this.projectService.unlockProjectInfrastructure(this.project, infrastructure).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          infrastructure.state = 'Unlocked';
          this.snackBar.open('Infrastructure unlocked.', '', {duration: 2000});
        } else {
          alert('Error unlocking layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error unlocking Infrastructure: ' + error.detail);
      });
    } else if (this.isBuildingGroup(layer)) {
      const buildingGroup = <BuildingGroup>layer;
      this.projectService.unlockProjectBuildingGroup(this.project, buildingGroup).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          buildingGroup.state = 'Unlocked';
          this.snackBar.open('Building group unlocked.', '', {duration: 2000});
        } else {
          alert('Error unlocking layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error unlocking Building group: ' + error.detail);
      });
    }
  }

  publishLayer(layer: BaseLayer) {
    if (this.isRailway(layer)) {
      const railway = <Railway> layer;
      this.projectService.publishProjectRailway(this.project, railway).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          railway.state = 'Published';
          this.snackBar.open('Railway published.', '', {duration: 2000});
        } else {
          alert('Error publishing layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error publishing Railway: ' + error.detail);
      });
    } else if (this.isInfrastructure(layer)) {
      const infrastructure = <RailwayInfrastructure> layer;
      this.projectService.publishProjectInfrastructure(this.project, infrastructure).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          infrastructure.state = 'Published';
          this.snackBar.open('Infrastructure published.', '', {duration: 2000});
        } else {
          alert('Error publishing layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error publishing Infrastructure: ' + error.detail);
      });
    } else if (this.isClutter(layer)) {
      const clutter = <Clutter> layer;
      this.projectService.publishProjectClutter(this.project, clutter).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          clutter.state = 'Published';
          this.snackBar.open('Clutter published.', '', {duration: 2000});
        } else {
          alert('Error publishing layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error publishing Clutter: ' + error.detail);
      });
    } else if (this.isBuildingGroup(layer)) {
      const buildingGroup = <BuildingGroup> layer;
      this.projectService.publishProjectBuildingGroup(this.project, buildingGroup).subscribe(res => {
        if (res.status >= 200 && res.status < 300) {
          buildingGroup.state = 'Published';
          this.snackBar.open('Building group published.', '', {duration: 2000});
        } else {
          alert('Error publishing layer: ' + res.statusText);
        }
      },
      err => {
        const error = JSON.parse(err.error);
        alert('Error publishing Building group: ' + error.detail);
      });
    }
  }

  addToPE(layer: BaseLayer) {
    alert('Add layer to PE:' + layer.name);
  }

  isRailway(layer: BaseLayer) {
    return 'railwayPoints' in layer;
  }

  isInfrastructure(layer: BaseLayer) {
    return ('railwayElementsHref' in layer);
  }

  isBuildingGroup(layer: BaseLayer) {
    return ('buildingsHref' in layer);
  }

  isClutter(layer: BaseLayer) {
    return ('clutterTilesHref' in layer);
  }

  editLayer(layer: BaseLayer) {
    const isRailway = ('railwayPoints' in layer);
    const isClutter = ('clutterTilesHref' in layer);
    const isInfrastructure = ('railwayElementsHref' in layer);
    const isBuildingGroup = ('buildingsHref' in layer);

    if (isRailway || isInfrastructure || isBuildingGroup) {
      layer.properties.editMode = true;
      if (isRailway) {
        this.projectService.updateProjectRailwayProperties(this.project, <Railway>layer).subscribe(
          () => {},
          err => {
            this.snackBar.open('Error saving Railway properties: ' + err, '', {duration: 2000});
            delete layer.properties.editMode;
          }
        );
      } else if (isInfrastructure) {
        this.projectService.updateProjectRailwayInfrastructureProperties(this.project, <RailwayInfrastructure>layer).subscribe(
          () => {},
          err => {
            this.snackBar.open('Error saving Infrastructure properties: ' + err, '', {duration: 2000});
            delete layer.properties.editMode;
          }
        );
      } else if (isBuildingGroup) {
        this.projectService.updateProjectBuildingGroupProperties(this.project, <BuildingGroup>layer).subscribe(
          () => {},
          err => {
            this.snackBar.open('Error saving BuildingGroup properties: ' + err, '', {duration: 2000});
            delete layer.properties.editMode;
          }
        );
      }
    } else if (isClutter) { // not possible
        // this.clutter = <Clutter>layer;
    }
  }

  editLayerCancel(layer: BaseLayer) {
    const isRailway = ('railwayPoints' in layer);
    const isClutter = ('clutterTilesHref' in layer);
    const isInfrastructure = ('railwayElementsHref' in layer);
    const isBuildingGroup = ('buildingsHref' in layer);
    if (isRailway || isInfrastructure || isBuildingGroup) {
      delete layer.properties.editMode;
      if (isRailway) {
        this.projectService.updateProjectRailwayProperties(this.project, <Railway>layer).subscribe(
          () => {},
          err => {
            this.snackBar.open('Error saving railway properties: ' + err.error, '', {duration: 2000});
            layer.properties.editMode = true;
          }
        );
      } else if (isInfrastructure) {
        this.projectService.updateProjectRailwayInfrastructureProperties(this.project, <RailwayInfrastructure>layer).subscribe(
          () => {},
          err => {
            this.snackBar.open('Error saving infrastructure properties: ' + err.error, '', {duration: 2000});
            layer.properties.editMode = true;
          }
        );
      } else if (isBuildingGroup) {
        this.projectService.updateProjectBuildingGroupProperties(this.project, <BuildingGroup>layer).subscribe(
          () => {
            const buildings = this.buildings.get(<BuildingGroup>layer);
            buildings.forEach((building) => {
              if (building.properties) { delete building.properties.editMode; }
            });
          },
          err => {
            this.snackBar.open('Error saving BuildingGroup properties: ' + err.error, '', {duration: 2000});
            layer.properties.editMode = true;
          }
        );
      }
    } else if (isClutter) { // not possible
//      this.clutter = <Clutter>layer;
    }
  }

  railwayMenuClickHandler(event) {
    const isRailway = ('railwayPoints' in event.layer);
    const isClutter = ('clutterTilesHref' in event.layer);
    const isInfrastructure = ('railwayElementsHref' in event.layer);
    if (event.action === 'Edit') {
      if (isRailway || isInfrastructure) {
//        this.selectRailway(event.layer);
        event.layer.properties.editMode = true;
//      } else if (isClutter) {
//        this.clutter = event.layer;
//      } else if (isInfrastructure) {
//        this.infrastructure = event.layer;
      }
    } else if (event.action === 'Center') {
//      if (isInfrastructure) {
//        this.fitBoundsInfrastructure(event.layer);
//      } else {
//        this.fitBoundsRailway(event.layer);
//      }
      this.fitBoundsLayer(event.layer);
    } else if (event.action === 'Hide') {
      event.layer.properties.visible = false;
      if (isRailway) {
        // this.updatePolyLine(event.layer);
      } else if (isClutter) {
        this.clutterMapLayers.get(event.layer).removeFrom(this.map);
      } else if (isInfrastructure) {
        this.infrastructureMapLayerGroups.get(event.layer).removeFrom(this.map);
      }
    } else if (event.action === 'Show') {
      event.layer.properties.visible = true;
      if (isRailway) {
          // this.updatePolyLine(event.layer);
      } else if (isClutter) {
        this.clutterMapLayers.get(event.layer).addTo(this.map);
      } else if (isInfrastructure) {
        this.infrastructureMapLayerGroups.get(event.layer).addTo(this.map);
      }
    } else if (event.action === 'AddElement') {
      const newElement = new RailwayElement();
      newElement.id = 0;
      newElement.properties = this.defaultRailwayProperties;
      const dialogRef = this.dialog.open(ElementpropertiesdialogComponent, {
        data: [this.project, this.infrastructures[0], newElement]
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result === 'OK') {
//          _comp.isElements[0].push(newElement);
//          _comp.infrastructureElements.get(this.infrastructure).push(newElement);
  //        _comp.snackBar.open('Railway created.', '', { duration: 2000 });
        } else {
  //        this.snackBar.open('Railway creation canceled.', '', { duration: 2000 });
        }
      });
    } else if (event.action === 'AddToScenario') {
//      for (let i = 0; i < this.railways.length; i++) {
//        if (this.railways[i] === event.layer) {
//          this.railwayEditCancel();
//          this.scenario.push(event.layer);
//          this.railways.splice(i, 1);
//          break;
//        }
//      }
    } else if (event.action === 'RemoveFromScenario') {
      for (let i = 0; i < this.peLayers.length; i++) {
        if (this.peLayers[i] === event.layer) {
          this.railways.push(event.layer);
          this.peLayers.splice(i, 1);
          break;
        }
      }
    } else if (event.action === 'MoveUp') {
      this.layerMoveUp(event.layer);
    } else if (event.action === 'MoveDown') {
      this.layerMoveDown(event.layer);
    } else if (event.action === 'Properties') {
      this.layerProperties(event.layer);
    } else if (event.action === 'Save') {
      if (isRailway) {
        this.saveRailway(event.layer);
      } else if (isClutter) {
        this.saveClutterClasses(event.layer);
      } else {
        this.saveInfrastructure(event.layer);
      }
    } else if (event.action === 'Remove') {
    } else {
      alert(event.action + ':' + event.layer.name);
    }
  }

  saveLayer(layer: BaseLayer) {
    if (this.isRailway(layer)) {
      this.saveRailway(<Railway>layer);
    }
    if (this.isInfrastructure(layer)) {
      this.saveInfrastructure(<RailwayInfrastructure>layer);
    }
    if (this.isClutter(layer)) {
      this.saveClutterClasses(<Clutter>layer);
    }
  }

  getLayerPos (layer: BaseLayer) {
    const lType = this.layerType(layer);
    let targetPos = -1;
    if (this.project.properties.layerOrder) {
      this.project.properties.layerOrder.forEach((element, index) => {
        if (element[0] === lType && element[1] === layer.id) {
          targetPos = index;
        }
      });
    }
    return targetPos;
  }

  insertLayer(layer: BaseLayer, array) {
    // array.splice(this.layerPosition(layer, array), 0, layer);
    if (array === this.layers) {
      const targetPos = this.getLayerPos(layer);
      let i = 0;
      for (; i < array.length; i++) {
        const cPos = this.getLayerPos(array[i]);
        if (cPos > targetPos) { break; }
      }
      array.splice(i, 0, layer);
    } else {
      array.push(layer);
    }
  }

  layerMoveUp(layer) {
    let last = this.layers[0];
    for (let i = 1; i < this.layers.length; i++) {
      if (this.layers[i] === layer) {
        this.layers[i - 1] = layer;
        this.layers[i] = last;
        break;
      }
      last = this.layers[i];
    }
    this.saveProjectProperties(true);
  }

  layerMoveDown(layer) {
    let last = this.layers[this.layers.length - 1];
    for (let i = this.layers.length - 2; i >= 0; i--) {
      if (this.layers[i] === layer) {
        this.layers[i + 1] = layer;
        this.layers[i] = last;
        break;
      }
      last = this.layers[i];
    }
    this.saveProjectProperties(true);
  }

  railwayEditChange(railway: Railway, reindex?: boolean) {
//    this.updatePolyLine(railway);
    if (reindex) { this.setRailwayPointsIdx(railway); }
  }

  railwayEditCancel(railway: Railway) {
    railway.properties.editMode = false;
  }

  railwayEditPoint(railway: Railway, feature: Feature<Point>) {
    const dialogRef = this.dialog.open(PointeditdialogComponent, {
      data: feature
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        railway.properties.pointsChanged = true;
        railway.properties.pointsEditChanged = true;
        this.saveRailway(railway);
      }
    });
  }

  railwayPointDelete(railway: Railway, feature: Feature<Point>) {
    railway.railwayPoints.features.splice(railway.railwayPoints.features.findIndex(f => f === feature), 1);
    railway.properties.pointsChanged = true;
    railway.properties.pointsEditChanged = true;
  }


  infrastructureEditSave(infrastructure: RailwayInfrastructure) {
    delete infrastructure.properties.editMode;
    this.saveInfrastructure(infrastructure);
  }

  infrastructureEditCancel(infrastructure: RailwayInfrastructure) {
    delete infrastructure.properties.editMode;
  }

  infrastructureAddElement(infrastructure: RailwayInfrastructure) {
    infrastructure.properties.action = 'addElement';
  }

  screenShot() {
//    html2canvas(document.body).then(function (canvas) {
//      const imgData = canvas.toDataURL('image/png');
//      document.body.appendChild(canvas);
//    });
  }

  closeProject() {
    this.appService.mainTitle = '';
    this.router.navigate(['home/projects']);
  }

  toggleNav() {
    this.sidenavOpened = !this.sidenavOpened;
  }

  onKey(event) { // with type info
    // if (this.railwayEdit && this.railwayEdit.railwayPoint) {
    //   this.railwayEdit.onKey(event);
    // }
  }

  public onElementContextMenu($event: MouseEvent, element: RailwayElement): void {
    this.contextMenuService.show.next({
      contextMenu: this.elementContextMenu,
      event: $event,
      item: element,
    });
    $event.preventDefault();
    $event.stopPropagation();
  }

  public elementProperties(infrastructure: RailwayInfrastructure, element: RailwayElement) {
    const dialogRef = this.dialog.open(ElementpropertiesdialogComponent, {
      data: [infrastructure, element]
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        if (element.properties.kilometricPoint && infrastructure.properties.parentRailwayID) {
          for (const r of this.railways) {
            if (r.id === infrastructure.properties.parentRailwayID) {
              for (const f of r.railwayPoints.features) {
                 if (f.properties.kilometricPoint === element.properties.kilometricPoint) {
                   element.location.geometry.coordinates[0] = f.geometry.coordinates[0];
                   element.location.geometry.coordinates[1] = f.geometry.coordinates[1];
                 }
              }
            }
          }
        }
        this.projectService.updateProjectRailwayInfrastructureElement(this.project, infrastructure, element).subscribe();
        infrastructure.properties.elementsChanged = true;
      }
    });
  }

  deleteElement(infrastructure: RailwayInfrastructure, element: RailwayElement) {
    this.infrastructureElementsLayersMap.get(infrastructure).get(element).removeFrom(this.map);
    this.infrastructureElementsLayersMap.get(infrastructure).delete(element);
    const elements: Array<RailwayElement> = this.infrastructureElements.get(infrastructure);
    // const deletedElements: Array<RailwayElement> = this.infrastructureDeletedElements.get(infrastructure);
    // deletedElements.push(elements.splice(elements.indexOf(element), 1)[0]);
    elements.splice(elements.indexOf(element), 1);
    this.projectService.delProjectRailwayInfrastructureElement(this.project, infrastructure, element).subscribe(resp => {
      if (resp.status >= 200 && resp.status < 300) {
      } else {alert('Error deleting element:' + resp.statusText); }
    },
    error => {
      alert('Error deleting railway element: ' + JSON.stringify(error));
    });
  }

  newBuildingGroup() {
    const newBuildingGroup = new BuildingGroup();
    newBuildingGroup.id = 0;
    newBuildingGroup.coordinateSystem = 4326;
    newBuildingGroup.state = 'Unlocked';
    newBuildingGroup.properties = this.defaultRailwayProperties;
    newBuildingGroup.buildingsHref = '';
    const dialogRef = this.dialog.open(LayerpropertiesdialogComponent, {
      data: [this.project, newBuildingGroup]
    });
    const _comp = this;
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        newBuildingGroup.properties = JSON.stringify(newBuildingGroup.properties);
        this.projectService.addProjectBuildingGroup(_comp.project, newBuildingGroup).subscribe(
          resp => {
            if (resp.status >= 200 && resp.status < 300) {
              this.projectService.getProjectBuildingGroup(resp.headers.get('Location')).subscribe( buildingGroup => {
                buildingGroup.properties = JSON.parse(buildingGroup.properties);
                _comp.addBuildingGroup(buildingGroup);
                this.snackBar.open('BuildingGroup created.', '', { duration: 2000 });
              });
            } else {
              alert('Error creating building group:' + resp.statusText);
            }
          }
        );
      } else {
        this.snackBar.open('Building group creation canceled.', '', { duration: 2000 });
      }
    });
  }


  editBuilding(buildingGroup: BuildingGroup, building: Building) {
    // buildingGroup.properties.editBuilding = building.id;
    if (building.properties) {
      building.properties.editMode = true;
    } else {
      building.properties = {editMode: true};
    }
  }

  editBuildingCancel(buildingGroup: BuildingGroup, building: Building) {
    delete building.properties.editMode;
  }

  deleteBuilding(buildingGroup: BuildingGroup, building: Building) {
    const buildings = this.buildings.get(buildingGroup);
    buildings.splice(buildings.indexOf(building), 1);
    this.projectService.delProjectBuildingGroupBuilding(this.project, buildingGroup, building).subscribe(
      () => { this.snackBar.open(`Building deleted`, '', {duration: 2000}); },
      (err) => {
        const error = JSON.parse(err.error);
        this.snackBar.open(`Error deleting building: ${error.detail}`, '', {duration: 2000});
      }
    );
  }

  public buildingProperties(buildingGroup: BuildingGroup, building: Building) {
    const dialogRef = this.dialog.open(BuildingpropertiesdialogComponent, {
      data: [buildingGroup, building]
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        buildingGroup.properties.buildingsChanged = true;
      }
    });
  }

  buildingEditPoint(railway: Railway, feature: Feature<Point>) {
    const dialogRef = this.dialog.open(PointeditdialogComponent, {
      data: feature
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'OK') {
        railway.properties.pointsChanged = true;
        railway.properties.pointsEditChanged = true;
      }
    });
  }

  isDeleteBuildingPointPossible(building): boolean {
    return (building.perimeter.coordinates[0].length > 2);
  }

  buildingPointDelete(building: Building, pointIdx: number) {
    building.perimeter.coordinates[0].splice(pointIdx, 1);
    building.properties.pointsChanged = true;
  }

  saveBuildingGroupProperties(buildingGroup: BuildingGroup, success?) {
    this.projectService.updateProjectBuildingGroupProperties(this.project, buildingGroup).subscribe(
      resp => {
        if (resp.status >= 200 && resp.status < 300) {
          if (success) {success(); }
        } else { alert('Error patching building group properties:' + resp.statusText); }
      }
    );
  }
  isBuildingEditPossible = (item): boolean => {
    return (!('properties' in item[1] && item[1].properties && 'editMode' in item[1].properties && item[1].properties.editMode));
  }

  isBuildingEditActive = (item): boolean => {
    return ('properties' in item[1] && item[1].properties && 'editMode' in item[1].properties && item[1].properties.editMode);
  }

  isPointBeingAdded() {
    return (this.railwayEdit && this.railwayEdit.find(rec => rec.railwayNewPoint != null));
  }
}
