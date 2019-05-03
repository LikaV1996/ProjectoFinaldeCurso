import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ObuDetailComponent } from './obu-detail.component';

describe('ObuDetailComponent', () => {
  let component: ObuDetailComponent;
  let fixture: ComponentFixture<ObuDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ObuDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ObuDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
