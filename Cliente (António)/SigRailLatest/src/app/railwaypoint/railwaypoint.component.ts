import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Feature, Point } from 'geojson';

@Component({
  selector: 'app-railwaypoint',
  templateUrl: './railwaypoint.component.html',
  styleUrls: ['./railwaypoint.component.css']
})
export class RailwaypointComponent implements OnInit {
  @Input() feature: Feature<Point>;
  @Output() coordsChange = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  coordsChanged() {
    this.coordsChange.emit();
  }
}
