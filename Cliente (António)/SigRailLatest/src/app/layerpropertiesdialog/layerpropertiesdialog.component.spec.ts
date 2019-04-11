import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LayerpropertiesdialogComponent } from './layerpropertiesdialog.component';

describe('LayerpropertiesdialogComponent', () => {
  let component: LayerpropertiesdialogComponent;
  let fixture: ComponentFixture<LayerpropertiesdialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LayerpropertiesdialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LayerpropertiesdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
