import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LayerlistdialogComponent } from './layerlistdialog.component';

describe('LayerlistdialogComponent', () => {
  let component: LayerlistdialogComponent;
  let fixture: ComponentFixture<LayerlistdialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LayerlistdialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LayerlistdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
