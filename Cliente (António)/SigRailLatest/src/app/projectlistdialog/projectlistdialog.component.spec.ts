import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectlistdialogComponent } from './projectlistdialog.component';

describe('ProjectlistdialogComponent', () => {
  let component: ProjectlistdialogComponent;
  let fixture: ComponentFixture<ProjectlistdialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectlistdialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectlistdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
