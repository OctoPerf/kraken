import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkspaceComponent} from './workspace.component';
import {GatlingConfigurationService} from 'projects/gatling/src/app/gatling-configuration.service';
import {gatlingConfigurationServiceSpy} from 'projects/gatling/src/app/gatling-configuration.service.spec';

describe('WorkspaceComponent', () => {
  let component: WorkspaceComponent;
  let fixture: ComponentFixture<WorkspaceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [WorkspaceComponent],
      providers: [
        {provide: GatlingConfigurationService, useValue: gatlingConfigurationServiceSpy()}
      ]
    })
      .overrideTemplate(WorkspaceComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkspaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
