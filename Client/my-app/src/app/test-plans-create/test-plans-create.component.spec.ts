import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestPlansCreateComponent } from './test-plans-create.component';

describe('TestPlansCreateComponent', () => {
  let component: TestPlansCreateComponent;
  let fixture: ComponentFixture<TestPlansCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestPlansCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestPlansCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
