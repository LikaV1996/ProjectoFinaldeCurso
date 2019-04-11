import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PEDetailsDialogComponent } from './pedetails-dialog.component';

describe('PEDetailsDialogComponent', () => {
  let component: PEDetailsDialogComponent;
  let fixture: ComponentFixture<PEDetailsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PEDetailsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PEDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
