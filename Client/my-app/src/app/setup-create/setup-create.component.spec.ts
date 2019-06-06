import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SetupCreateComponent } from './setup-create.component';

describe('SetupCreateComponent', () => {
  let component: SetupCreateComponent;
  let fixture: ComponentFixture<SetupCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SetupCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SetupCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
