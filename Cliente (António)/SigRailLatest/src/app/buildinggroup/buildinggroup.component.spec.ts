import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildinggroupComponent } from './buildinggroup.component';

describe('BuildinggroupComponent', () => {
  let component: BuildinggroupComponent;
  let fixture: ComponentFixture<BuildinggroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildinggroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildinggroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
