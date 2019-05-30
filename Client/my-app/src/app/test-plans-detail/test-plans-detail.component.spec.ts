import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestPlansDetailComponent } from './test-plans-detail.component';

describe('TestPlansDetailComponent', () => {
  let component: TestPlansDetailComponent;
  let fixture: ComponentFixture<TestPlansDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestPlansDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestPlansDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
