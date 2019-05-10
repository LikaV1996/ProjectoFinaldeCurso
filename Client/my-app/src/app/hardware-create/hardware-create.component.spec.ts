import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HardwareCreateComponent } from './hardware-create.component';

describe('HardwareCreateComponent', () => {
  let component: HardwareCreateComponent;
  let fixture: ComponentFixture<HardwareCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HardwareCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HardwareCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
