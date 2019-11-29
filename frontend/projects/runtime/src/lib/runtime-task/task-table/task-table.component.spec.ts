import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskTableComponent } from './task-table.component';

describe('TaskTableComponent', () => {
  let component: TaskTableComponent;
  let fixture: ComponentFixture<TaskTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
