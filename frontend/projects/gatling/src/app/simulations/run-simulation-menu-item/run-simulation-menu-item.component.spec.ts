import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RunSimulationMenuItemComponent} from './run-simulation-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';

describe('RunSimulationMenuItemComponent', () => {
  let component: RunSimulationMenuItemComponent;
  let fixture: ComponentFixture<RunSimulationMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RunSimulationMenuItemComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: SimulationService, useValue: simulationServiceSpy()},
      ]
    })
      .overrideTemplate(RunSimulationMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunSimulationMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
