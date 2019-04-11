import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildingpropertiesdialogComponent } from './buildingpropertiesdialog.component';

describe('BuildingpropertiesdialogComponent', () => {
  let component: BuildingpropertiesdialogComponent;
  let fixture: ComponentFixture<BuildingpropertiesdialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildingpropertiesdialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildingpropertiesdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
