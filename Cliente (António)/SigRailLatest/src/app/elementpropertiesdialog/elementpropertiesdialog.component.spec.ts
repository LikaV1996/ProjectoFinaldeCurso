import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElementpropertiesdialogComponent } from './elementpropertiesdialog.component';

describe('ElementpropertiesdialogComponent', () => {
  let component: ElementpropertiesdialogComponent;
  let fixture: ComponentFixture<ElementpropertiesdialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElementpropertiesdialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElementpropertiesdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
