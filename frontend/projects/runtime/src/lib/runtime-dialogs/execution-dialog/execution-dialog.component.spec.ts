import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecutionDialogComponent } from './execution-dialog.component';

describe('ExecutionDialogComponent', () => {
  let component: ExecutionDialogComponent;
  let fixture: ComponentFixture<ExecutionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExecutionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecutionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
