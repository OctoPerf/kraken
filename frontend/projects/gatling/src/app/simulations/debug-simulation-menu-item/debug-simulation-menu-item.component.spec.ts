import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DebugSimulationMenuItemComponent} from 'projects/gatling/src/app/simulations/debug-simulation-menu-item/debug-simulation-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';

describe('DebugSimulationMenuItemComponent', () => {
  let component: DebugSimulationMenuItemComponent;
  let fixture: ComponentFixture<DebugSimulationMenuItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DebugSimulationMenuItemComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: SimulationService, useValue: simulationServiceSpy()},
      ]
    })
      .overrideTemplate(DebugSimulationMenuItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DebugSimulationMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
