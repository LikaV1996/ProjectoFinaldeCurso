import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ServerlogComponent } from './serverlog.component';

describe('ServerlogComponent', () => {
  let component: ServerlogComponent;
  let fixture: ComponentFixture<ServerlogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ServerlogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ServerlogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
