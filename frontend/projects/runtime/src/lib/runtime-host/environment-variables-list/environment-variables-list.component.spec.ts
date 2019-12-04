import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EnvironmentVariablesListComponent } from './environment-variables-list.component';

describe('EnvironmentVariablesListComponent', () => {
  let component: EnvironmentVariablesListComponent;
  let fixture: ComponentFixture<EnvironmentVariablesListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnvironmentVariablesListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnvironmentVariablesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
