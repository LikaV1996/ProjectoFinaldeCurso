import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { CoordinateSystem, Project, Railway,
    RailwayInfrastructure, Clutter, RailwayElement,
    RailwayElementClass, BuildingGroup, Building, PropagationEnvironment, Tunnel } from './model/project';

import { Observable, throwError } from 'rxjs';
import { MatSnackBar } from '@angular/material';
import { tap, map, catchError } from 'rxjs/operators';

const httpHeaders = new HttpHeaders({
    'Content-Type':  'application/json',
    'Api-Key': 'key'
});

const httpImgHeaders = new HttpHeaders({
//    'Content-Type':  'image/png',
    'Api-Key': 'key'
  });
const httpOptions = {
  headers: httpHeaders
};

class LogEntry {
    public date: Date;
    constructor(public message: string, public type?: string) {
      this.date = new Date();
    }
  }

@Injectable()
export class ProjectService {

  private serverUrl = 'http://192.68.221.229';
  private projectUrl = this.serverUrl + '/solvittool/api/project';
  private railwayUrl = this.serverUrl + '/solvittool/api/railway';
  private infrastructureUrl = this.serverUrl + '/solvittool/api/railway-infrastructure';
  private buildingGroupUrl = this.serverUrl + '/solvittool/api/building-group';
  private clutterUrl = this.serverUrl + '/solvittool/api/clutter';
  private coordinateSystemUrl = this.serverUrl + '/solvittool/api/coordinate-system';
  private elementClassesUrl = this.serverUrl + '/solvittool/api/railway-element-class';

  public logEntries: LogEntry[] = [];

  coordinateSystems: CoordinateSystem[];
  railwayElementClasses: RailwayElementClass[];
  public pointAddCursor = false;

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
    this.http.get<CoordinateSystem[]>(this.coordinateSystemUrl, {headers: httpHeaders}).pipe(
        catchError(this.handleError('Loading Coordinate systems'))).subscribe(
      css => { this.coordinateSystems = css; }
    );
    this.http.get<RailwayElementClass[]>(this.elementClassesUrl, {headers: httpHeaders}).pipe(
        catchError(this.handleError('Loading element classes'))).subscribe(
      rec => { this.railwayElementClasses = rec; }
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  /*private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead
      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }*/

  public log(message: string, type?: string) {
    this.logEntries.push(new LogEntry(message, type || 'Info'));
  }

  private handleError(operation: string) {
    return (error: HttpErrorResponse) => {
      // console.error('Error in ' + operation + ': ' + JSON.stringify(error));
      // console.error(error.error);
      this.log('Error in ' + operation + ': ' + error.message, 'Error');
      console.error('Error in ' + operation + ': ' + error.message);
      this.snackBar.open(`${operation} failed with http Error: ${error.status}; ${error.error.detail}`, '', {duration: 4000});
      return throwError(error);
    };
  }

 /* private handleError_old(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred: ', error.error.message);
      this.snackBar.open(`Http Error: ${error.error.message}`, '', {duration: 2000});
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      this.snackBar.open(`Http Error: ${error.status}; ${error.error}`, '', {duration: 2000});
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError('Something bad happened; please try again later.');
  }*/

// PROJECTS
  getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.projectUrl, httpOptions).pipe(
        catchError(this.handleError('Get all projects')));
  }

  filterAllProjects(filt): Observable<Project[]> {
    return this.http.post<Project[]>(this.projectUrl + '/filter', filt, httpOptions).pipe(
        catchError(this.handleError('Filter all projects')));
  }

  addProject (project: Project): Observable<HttpResponse<any>> {
    return this.http.post<Project>(this.projectUrl, project, {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Create project')));
  }

  delProject (project: Project): Observable<HttpResponse<any>> {
    return this.http.delete(this.projectUrl + '/' + project['id'],
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Delete Project')));
  }

  getProject (ref: number | string): Observable<Project> {
    const obs: Observable<Project> = typeof ref === 'number' ?
      this.http.get<Project>(this.projectUrl + '/' +  ref, httpOptions) :
      this.http.get<Project>(ref, httpOptions);
    return obs.pipe(
      map(project => { project.properties = JSON.parse(project.properties); return project; } ),
      catchError(this.handleError('Get Project'))
    );
  }

  updateProject(project: Project): Observable<HttpResponse<any>> {
    const p1 = {...project};
    p1.properties = JSON.stringify(project.properties);
    return this.http.put(this.projectUrl + '/' + project.id, p1,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update Project')));
  }

  updateProjectProperties (project: Project): Observable<HttpResponse<Object>> { // OBSOLETE
    return this.http.patch<Object>(this.projectUrl + '/' + project.id + '/properties',
      { properties: JSON.stringify(project.properties) }, {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update project properties')));
  }

  lockProject (project: Project): Observable<HttpResponse<Project>> {
    return this.http.patch<Project>(this.projectUrl + '/' + project.id + '/lock', project, {observe: 'response'}).pipe(
        catchError(this.handleError('Lock project')));
  }

  publishProject (project: Project): Observable<HttpResponse<Project>> {
    return this.http.patch<Project>(this.projectUrl + '/' + project['@unid'] + '/publish', project, {observe: 'response'}).pipe(
        catchError(this.handleError('Publish Project')));
  }


// Propagation Environment
  addProjectPE(project: Project | string, pe: PropagationEnvironment): Observable<any> {
    const obs = typeof project === 'string' ?
      this.http.post(project, pe, {headers: httpHeaders, observe: 'response'}) :
      this.http.post(this.projectUrl + '/' + project.id + '/pe', pe, {headers: httpHeaders, observe: 'response'});
    return obs.pipe(catchError(this.handleError('Create PE')));
  }

  getProjectPE(project: Project | string): Observable<PropagationEnvironment> {
    if (typeof project === 'string') { return this.http.get<PropagationEnvironment>(project, httpOptions); }
    return this.http.get<PropagationEnvironment>(this.projectUrl + '/' + project.id + '/pe', httpOptions);
  }


// RAILWAYS
  getAllRailways(): Observable<Railway[]> {
    return this.http.get<Railway[]>(this.railwayUrl, httpOptions).pipe(catchError(this.handleError('Get all railways')));
  }

  filterAllRailways(filt): Observable<Railway[]> {
    return this.http.post<Railway[]>(this.railwayUrl + '/filter', filt, httpOptions).pipe(
      catchError(this.handleError('Filter all railways')));
  }

  getProjectRailways(project: Project): Observable<Railway[]> {
    return this.http.get<Railway[]>(project.railwaysHref, httpOptions).pipe(
        catchError(this.handleError('Get project railways')));
  }

  addProjectRailway (project: Project, railway: Railway): Observable<HttpResponse<any>> {
    return this.http.post(this.projectUrl + '/' + project.id + '/railway', railway,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('New railway')));
  }

  addPublishedRailway (project: Project, railway: Railway): Observable<HttpResponse<any>> {
    return this.http.post(this.projectUrl + '/' + project.id + '/railway/' + railway.id, {},
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Add published railway')));
  }

  cloneProjectRailway (project: Project, railway: Railway, newName: string): Observable<HttpResponse<any>> {
    return this.http.post(this.projectUrl + '/' + project.id + '/railway/' + railway.id + '/clone', { name: newName },
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Clone railway')));
  }

  getProjectRailway(project: Project | string, railway?: Railway): Observable<Railway> {
    if (typeof project === 'string') { return this.http.get<Railway>(project, httpOptions); }
    return this.http.get<Railway>(this.projectUrl + '/' + project.id + '/railway/' + railway.id, httpOptions).pipe(
      map((rw) => {
        rw.properties = JSON.parse(rw.properties);
        return rw;
      }),
      catchError(this.handleError('Get railway'))
    );
  }

  updateProjectRailway(project: Project, railway: Railway): Observable<HttpResponse<any>> {
    const r1 = {...railway};
    r1.properties = JSON.stringify(railway.properties);
    return this.http.put(this.projectUrl + '/' + project.id + '/railway/' + railway.id, r1,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update railway')));
  }

  updateProjectRailwayProperties(project: Project, railway: Railway): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project.id + '/railway/' + railway.id +
      '/properties', { properties: JSON.stringify(railway.properties) },
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update railway properties')));
  }

  delProjectRailway(project: Project, railway: Railway): Observable<HttpResponse<any>> {
    return this.http.delete(this.projectUrl + '/' + project['id'] + '/railway/' + railway.id ,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Delete railway')));
  }

  lockProjectRailway(project: Project, railway: Railway): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/railway/' + railway.id + '/lock' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Lock railway')));
  }

  unlockProjectRailway(project: Project, railway: Railway): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/railway/' + railway.id + '/unlock' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Unlock railway')));
  }

  publishProjectRailway(project: Project, railway: Railway): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/railway/' + railway.id + '/publish' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Publish railway')));
  }


  // RAILWAY INFRASTRUCTURE
  getAllInfrastructures(): Observable<RailwayInfrastructure[]> {
    return this.http.get<RailwayInfrastructure[]>(this.infrastructureUrl, httpOptions).pipe(
        catchError(this.handleError('Get all infrastructures')));
  }

  getProjectRailwayInfrastructures(project: Project): Observable<RailwayInfrastructure[]> {
    return this.http.get<RailwayInfrastructure[]>(project.railwayInfrastructuresHref, httpOptions).pipe(
        catchError(this.handleError('Get project infrastructures')));
  }

  getProjectRailwayInfrastructure(project: Project | string, infrastructure?: RailwayInfrastructure): Observable<RailwayInfrastructure> {
    return this.http.get<RailwayInfrastructure>(typeof project === 'string' ? project :
    this.projectUrl + '/' + project.id + '/railway-infrastructure/' + infrastructure.id, httpOptions).pipe(
        catchError(this.handleError('Get infrastructure')));
  }

  getRailwayInfrastructure(infrastructure: RailwayInfrastructure | string): Observable<RailwayInfrastructure> {
    return this.http.get<RailwayInfrastructure>(typeof infrastructure === 'string' ? infrastructure : infrastructure.href,
      httpOptions).pipe(
        map((inf) => {
          inf.properties = JSON.parse(inf.properties);
          return inf;
        }),
        catchError(this.handleError('Get infrastructure'))
      );
  }

  addProjectRailwayInfrastructure (project: Project, infrastructure: RailwayInfrastructure): Observable<any> {
    return this.http.post(this.projectUrl + '/' + project.id + '/railway-infrastructure', infrastructure,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Create infrastructure')));
  }

  cloneProjectRailwayInfrastructure (project: Project, infrastructure: RailwayInfrastructure, newName: string): Observable<any> {
    return this.http.post(this.projectUrl + '/' + project.id + '/railway-infrastructure/' + infrastructure.id + '/clone', { name: newName },
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Clone infrastructure')));
  }

  addPublishedRailwayInfrastructure (project: Project, infrastructure: RailwayInfrastructure): Observable<HttpResponse<any>> {
    return this.http.post(this.projectUrl + '/' + project.id + '/railway-infrastructure/' + infrastructure.id, {},
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Add published infrastructure')));
  }

  updateProjectRailwayInfrastructure(project: Project, infrastructure: RailwayInfrastructure): Observable<any> {
    const inf1 = {...infrastructure};
    inf1.properties = JSON.stringify(infrastructure.properties);
    return this.http.put(this.projectUrl + '/' + project.id + '/railway-infrastructure/' + infrastructure.id, inf1,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update infrastructure')));
  }

  updateProjectRailwayInfrastructureProperties(project: Project, infrastructure: RailwayInfrastructure): Observable<any> {
    return this.http.patch(this.projectUrl + '/' + project.id + '/railway-infrastructure/' + infrastructure.id +
      '/properties', {properties: JSON.stringify(infrastructure.properties)},
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update infrastructure properties')));
  }

  delProjectInfrastructure(project: Project, infrastructure: RailwayInfrastructure): Observable<HttpResponse<any>> {
    return this.http.delete(this.projectUrl + '/' + project['id'] + '/railway-infrastructure/' + infrastructure.id ,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Delete infrastructure')));
  }

  lockProjectInfrastructure(project: Project, infrastructure: RailwayInfrastructure): Observable<any> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/railway-infrastructure/' + infrastructure.id + '/lock' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Lock infrastructure')));
  }

  unlockProjectInfrastructure(project: Project, infrastructure: RailwayInfrastructure): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/railway-infrastructure/' + infrastructure.id + '/unlock' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Unlock infrastructure')));
  }

  publishProjectInfrastructure(project: Project, infrastructure: RailwayInfrastructure): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/railway-infrastructure/' + infrastructure.id + '/publish' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Publish infrastructure')));
  }

  filterAllInfrastructures(filt): Observable<RailwayInfrastructure[]> {
    return this.http.post<RailwayInfrastructure[]>(this.infrastructureUrl + '/filter', filt, httpOptions).pipe(
        catchError(this.handleError('Filter all infrastructures')));
  }

  // Railway Elements
  getRailwayInfrastructureElements(infrastructure: RailwayInfrastructure): Observable<any> {
    return this.http.get<any>(infrastructure.railwayElementsHref, httpOptions).pipe(
        catchError(this.handleError('Get infrastructure elements')));
  }

  getProjectRailwayInfrastructureElement(project: Project | string,
    infrastructure: RailwayInfrastructure, element: RailwayElement): Observable<RailwayElement> {
    return this.http.get<RailwayElement>(typeof project === 'string' ? project :
      this.projectUrl + '/' + project.id + '/railway-infrastructure/' + infrastructure.id + '/element/' + element.id,
      httpOptions).pipe(
      map(el => {
        el.properties = JSON.parse(el.properties);
        return el;
      }),
      catchError(this.handleError('Get railway element')));
  }

  getRailwayElement(element: RailwayElement | string): Observable<RailwayElement> {
    return this.http.get<RailwayElement>(typeof element === 'string' ? element : element.href, httpOptions).pipe(
      map(el => {
        el.properties = JSON.parse(el.properties);
        return el;
      }),
      catchError(this.handleError('Get element')));
  }

  addProjectRailwayInfrastructureElement(project: Project, infrastructure: RailwayInfrastructure,
    element: RailwayElement): Observable<HttpResponse<any>> {
    const e1 = { ...element };
    e1.properties = JSON.stringify(e1.properties);
    return this.http.post(this.projectUrl + '/' + project.id + '/railway-infrastructure/'
      + infrastructure.id + '/railway-element', e1,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Create infrastructure element')));
  }

  updateProjectRailwayInfrastructureElement(project: Project, infrastructure: RailwayInfrastructure,
    element: RailwayElement): Observable<any> {
    const e1 = { ...element };
    e1.properties = JSON.stringify(e1.properties);
    return this.http.put(this.projectUrl + '/' + project.id + '/railway-infrastructure/'
      + infrastructure.id + '/railway-element/' + element.id, e1,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update infrastructure element')));
  }

  delProjectRailwayInfrastructureElement (project: Project, infrastructure: RailwayInfrastructure,
    element: RailwayElement): Observable<any> {
    return this.http.delete(this.projectUrl + '/' + project['id'] + '/railway-infrastructure/'
      + infrastructure.id + '/railway-element/' + element.id ,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Delete element')));
  }

  // Railway Tunnels
  getRailwayInfrastructureTunnels(infrastructure: RailwayInfrastructure): Observable<any> {
    return this.http.get<any>(infrastructure.tunnelsHref, httpOptions).pipe(
        catchError(this.handleError('Get infrastructure tunnels')));
  }

  getProjectRailwayInfrastructureTunnel(project: Project | string,
    infrastructure: RailwayInfrastructure, tunnel: Tunnel): Observable<Tunnel> {
    return this.http.get<Tunnel>(typeof project === 'string' ? project :
      this.projectUrl + '/' + project.id + '/railway-infrastructure/' + infrastructure.id + '/tunnel/' + tunnel.id,
      httpOptions).pipe(
      map(el => {
        el.properties = JSON.parse(el.properties);
        return el;
      }),
      catchError(this.handleError('Get railway tunnel')));
  }

  getTunnel(tunnel: Tunnel | string): Observable<Tunnel> {
    return this.http.get<Tunnel>(typeof tunnel === 'string' ? tunnel : tunnel.href, httpOptions).pipe(
      map(el => {
        el.properties = JSON.parse(el.properties);
        return el;
      }),
      catchError(this.handleError('Get tunnel')));
  }

  addProjectRailwayInfrastructureTunnel(project: Project, infrastructure: RailwayInfrastructure,
    tunnel: Tunnel): Observable<HttpResponse<any>> {
    const t1 = { ...tunnel };
    t1.properties = JSON.stringify(t1.properties);
    return this.http.post(this.projectUrl + '/' + project.id + '/railway-infrastructure/'
      + infrastructure.id + '/tunnel', t1,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Create infrastructure tunnel')));
  }

  updateProjectRailwayInfrastructureTunnel(project: Project, infrastructure: RailwayInfrastructure,
    tunnel: Tunnel): Observable<any> {
    const t1 = { ...tunnel };
    t1.properties = JSON.stringify(t1.properties);
    return this.http.put(this.projectUrl + '/' + project.id + '/railway-infrastructure/'
      + infrastructure.id + '/tunnel/' + tunnel.id, t1,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update infrastructure tunnel')));
  }

  delProjectRailwayInfrastructureTunnel (project: Project, infrastructure: RailwayInfrastructure,
    tunnel: Tunnel): Observable<any> {
    return this.http.delete(this.projectUrl + '/' + project['id'] + '/railway-infrastructure/'
      + infrastructure.id + '/tunnel/' + tunnel.id ,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Delete tunnel')));
  }


  // CLUTTER
  getAllClutters(): Observable<Clutter[]> {
    return this.http.get<Clutter[]>(this.clutterUrl, httpOptions).pipe(
        catchError(this.handleError('Get all clutters')));
  }

  getProjectClutters(project: Project): Observable<Clutter[]> {
    return this.http.get<Clutter[]>(project.cluttersHref, httpOptions).pipe(
        catchError(this.handleError('Get project clutters')));
  }

  addPublishedClutter (project: Project, clutter: Clutter): Observable<HttpResponse<any>> {
    return this.http.post(this.projectUrl + '/' + project.id + '/clutter/' + clutter.id, {},
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Add published clutter')));
  }

  getProjectClutter(project: Project | string, clutter?: Clutter): Observable<Clutter> {
    if (typeof project === 'string') { return this.http.get<Clutter>(project, httpOptions); }
    return this.http.get<Clutter>(this.projectUrl + '/' + project.id + '/clutter/' + clutter.id, httpOptions).pipe(
        catchError(this.handleError('Get clutter')));
  }

  cloneProjectClutter (project: Project, clutter: Clutter, newName: string): Observable<any> {
    return this.http.post(this.projectUrl + '/' + project.id + '/clutter/' + clutter.id + '/clone', { name: newName },
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Clone clutter')));
  }

  /*addProjectRailway (project: Project, railway: Railway): Observable<any> {
    return this.http.post(this.projectUrl + '/' + project.id + '/railway', railway,
      {headers: httpHeaders, observe: 'response', responseType: 'text'});
  }*/

  updateProjectClutter(project: Project, clutter: Clutter): Observable<any> {
    return this.http.put(this.projectUrl + '/' + project.id + '/clutter/' + clutter.id, clutter,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update clutter')));
  }

  updateProjectClutterClasses(project: Project, clutter: Clutter): Observable<any> {
    return this.http.put(this.projectUrl + '/' + project.id + '/clutter/' + clutter.id + '/classes', {classes: clutter.classes},
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update clutter classes')));
  }

  updateProjectClutterProperties(project: Project, clutter: Clutter): Observable<any> {
    return this.http.patch(this.projectUrl + '/' + project.id + '/clutter/' + clutter.id +
      '/properties', {properties: JSON.stringify(clutter.properties)},
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Update clutter properties')));
  }

  delProjectClutter(project: Project, clutter: Clutter): Observable<HttpResponse<any>> {
    return this.http.delete(this.projectUrl + '/' + project['id'] + '/clutter/' + clutter.id ,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Delete clutter')));
  }

  lockProjectClutter(project: Project, clutter: Clutter): Observable<any> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/clutter/' + clutter.id + '/lock' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Lock clutter')));
  }

  unlockProjectClutter(project: Project, clutter: Clutter): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/clutter/' + clutter.id + '/unlock' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Unlock clutter')));
  }

  publishProjectClutter(project: Project, clutter: Clutter): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/clutter/' + clutter.id + '/publish' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Publish clutter')));
  }

  getClutterImg(url): Observable<Blob> {
    return this.http.get(url, {headers: httpImgHeaders, responseType: 'blob'});
  }

  filterAllClutters(filt): Observable<Clutter[]> {
    return this.http.post<Clutter[]>(this.clutterUrl + '/filter', filt, httpOptions).pipe(
        catchError(this.handleError('Filter all clutters')));
  }


  // BUILDING GROUP
  getAllBuildingGroups(): Observable<BuildingGroup[]> {
    return this.http.get<BuildingGroup[]>(this.buildingGroupUrl, httpOptions).pipe(
        catchError(this.handleError('Get all building groups')));
  }

  getProjectBuildingGroups(project: Project): Observable<BuildingGroup[]> {
    return this.http.get<BuildingGroup[]>(project.buildingGroupsHref, httpOptions).pipe(
        catchError(this.handleError('Get project building groups')));
  }

  addProjectBuildingGroup (project: Project, buildingGroup: BuildingGroup): Observable<HttpResponse<any>> {
    return this.http.post(this.projectUrl + '/' + project.id + '/building-group', buildingGroup,
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Add building group')));
  }

  addPublishedBuildingGroup (project: Project, buildingGroup: BuildingGroup): Observable<HttpResponse<any>> {
    return this.http.post(this.projectUrl + '/' + project.id + '/building-group/' + buildingGroup.id, {},
      {headers: httpHeaders, observe: 'response'}).pipe(
        catchError(this.handleError('Add published building group')));
  }

  getProjectBuildingGroup(project: Project | string, buildingGroup?: BuildingGroup): Observable<BuildingGroup> {
    if (typeof project === 'string') { return this.http.get<BuildingGroup>(project, httpOptions).pipe(
        catchError(this.handleError('Get building group'))); }
    return this.http.get<BuildingGroup>(this.projectUrl + '/' + project.id + '/building-group/' + buildingGroup.id, httpOptions).pipe(
        catchError(this.handleError('Get building group')));
  }

  getBuildingGroup(buildingGroup: BuildingGroup): Observable<BuildingGroup> {
    return this.http.get<BuildingGroup>(/*this.projectUrl + '/' + project.id + '/railway'*/buildingGroup.href, httpOptions).pipe(
        catchError(this.handleError('Get building group')));
  }

  getBuildingGroupBuildings(buildingGroup: BuildingGroup): Observable<Building[]> {
    // return this.http.get<any>(this.projectUrl + '/' + project.id + '/railway-infrastructure/' + infrastructure.id +
    //   '/railway-element', httpOptions);
    return this.http.get<Building[]>(buildingGroup.buildingsHref + '/building', httpOptions).pipe(
        catchError(this.handleError('Get buildings')));
  }

  updateProjectBuildingGroup(project: Project, buildingGroup: BuildingGroup): Observable<HttpResponse<any>> {
    const bg1 = {...buildingGroup};
    bg1.properties = JSON.stringify(buildingGroup.properties);
    return this.http.put(this.projectUrl + '/' + project.id + '/building-group/' + buildingGroup.id, bg1,
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Update building group')));
  }

  updateProjectBuildingGroupProperties(project: Project, buildingGroup: BuildingGroup): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project.id + '/building-group/' + buildingGroup.id +
      '/properties', {properties: JSON.stringify(buildingGroup.properties)},
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Update building group')));
  }

  cloneProjectBuildingGroup (project: Project, buildingGroup: BuildingGroup, newName: string): Observable<HttpResponse<any>> {
    return this.http.post(this.projectUrl + '/' + project.id + '/building-group/' + buildingGroup.id + '/clone', { name: newName },
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Clone building group')));
  }

  delProjectBuildingGroup(project: Project, buildingGroup: BuildingGroup): Observable<HttpResponse<any>> {
    return this.http.delete(this.projectUrl + '/' + project['id'] + '/building-group/' + buildingGroup.id ,
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Delete building group')));
  }

  lockProjectBuildingGroup(project: Project, buildingGroup: BuildingGroup): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/building-group/' + buildingGroup.id + '/lock' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Lock building group')));
  }

  unlockProjectBuildingGroup(project: Project, buildingGroup: BuildingGroup): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/building-group/' + buildingGroup.id + '/unlock' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Unlock building group')));
  }

  publishProjectBuildingGroup(project: Project, buildingGroup: BuildingGroup): Observable<HttpResponse<any>> {
    return this.http.patch(this.projectUrl + '/' + project['id'] + '/building-group/' + buildingGroup.id + '/publish' , '',
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Publish building group')));
  }

  filterAllBuildingGroups(filt): Observable<BuildingGroup[]> {
    return this.http.post<BuildingGroup[]>(this.buildingGroupUrl + '/filter', filt, httpOptions)
    .pipe(catchError(this.handleError('Filter building groups')));
  }

  // BUILDING
  getProjectBuildingGroupBuilding(project: Project | string,
    buildingGroup?: BuildingGroup, building?: Building): Observable<Building> {
    return this.http.get<Building>(typeof project === 'string' ? project :
      (this.projectUrl + '/' + project.id + '/building-group/' + buildingGroup.id + '/building/' + building.id), httpOptions).pipe(
        map(b => {
          b.properties = JSON.parse(b.properties);
          return b;
        }),
        catchError(this.handleError('Get building')));
  }

  addProjectBuildingGroupBuilding(project: Project, buildingGroup: BuildingGroup,
    building: Building): Observable<HttpResponse<any>> {
    const b1 = {...building};
    b1.properties = JSON.stringify(building.properties);
    b1.perimeter = {
      coordinates: [[...building.perimeter.coordinates[0], Object.assign([], building.perimeter.coordinates[0][0])]],
      type: 'Point'
    };
    return this.http.post(this.projectUrl + '/' + project.id + '/building-group/'
      + buildingGroup.id + '/building', b1,
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Create building')));
  }

  updateProjectBuildingGroupBuilding(project: Project, buildingGroup: BuildingGroup, building: Building): Observable<HttpResponse<any>> {
    const b1 = {...building};
    b1.properties = JSON.stringify(building.properties);
    b1.perimeter = {
      coordinates: [[...building.perimeter.coordinates[0], Object.assign([], building.perimeter.coordinates[0][0])]],
      type: 'Point'
    };
    return this.http.put(this.projectUrl + '/' + project.id + '/building-group/' + buildingGroup.id + '/building/' + building.id, b1,
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Update Building')));
  }

  delProjectBuildingGroupBuilding (project: Project, buildingGroup: BuildingGroup,
    building: Building): Observable<any> {
    return this.http.delete(this.projectUrl + '/' + project['id'] + '/building-group/'
      + buildingGroup.id + '/building/' + building.id ,
      {headers: httpHeaders, observe: 'response'}).pipe(catchError(this.handleError('Delete building')));
  }
}
