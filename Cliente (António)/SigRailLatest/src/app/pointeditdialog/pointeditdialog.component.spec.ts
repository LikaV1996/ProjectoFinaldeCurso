import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PointeditdialogComponent } from './pointeditdialog.component';

describe('PointeditdialogComponent', () => {
  let component: PointeditdialogComponent;
  let fixture: ComponentFixture<PointeditdialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PointeditdialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PointeditdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
