import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ObuCreateComponent } from './obu-create.component';

describe('ObuCreateComponent', () => {
  let component: ObuCreateComponent;
  let fixture: ComponentFixture<ObuCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ObuCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ObuCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
