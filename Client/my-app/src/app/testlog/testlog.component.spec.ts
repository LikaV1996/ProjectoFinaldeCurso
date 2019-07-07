import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestLogComponent } from './testlog.component';

describe('TestLogComponent', () => {
  let component: TestLogComponent;
  let fixture: ComponentFixture<TestLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestLogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
