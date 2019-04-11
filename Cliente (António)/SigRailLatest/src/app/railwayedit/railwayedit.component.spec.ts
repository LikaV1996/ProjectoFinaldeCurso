import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RailwayeditComponent } from './railwayedit.component';

describe('RailwayeditComponent', () => {
  let component: RailwayeditComponent;
  let fixture: ComponentFixture<RailwayeditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RailwayeditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RailwayeditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
