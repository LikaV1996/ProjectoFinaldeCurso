import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectdetailsdialogComponent } from './projectdetailsdialog.component';

describe('ProjectdetailsdialogComponent', () => {
  let component: ProjectdetailsdialogComponent;
  let fixture: ComponentFixture<ProjectdetailsdialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectdetailsdialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectdetailsdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
