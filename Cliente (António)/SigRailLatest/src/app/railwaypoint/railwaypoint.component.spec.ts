import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RailwaypointComponent } from './railwaypoint.component';

describe('RailwaypointComponent', () => {
  let component: RailwaypointComponent;
  let fixture: ComponentFixture<RailwaypointComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RailwaypointComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RailwaypointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
