import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OBUComponent } from './obu.component';

describe('ObuComponent', () => {
  let component: OBUComponent;
  let fixture: ComponentFixture<OBUComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OBUComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OBUComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
