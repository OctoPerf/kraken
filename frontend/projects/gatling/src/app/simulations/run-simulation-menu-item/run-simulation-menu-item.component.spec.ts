import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RunSimulationMenuItemComponent} from './run-simulation-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import SpyObj = jasmine.SpyObj;

describe('RunSimulationMenuItemComponent', () => {
  let component: RunSimulationMenuItemComponent;
  let fixture: ComponentFixture<RunSimulationMenuItemComponent>;
  let treeControl: SpyObj<StorageTreeControlService>;
  let simulation: SpyObj<SimulationService>;

  beforeEach(async(() => {
    treeControl = storageTreeControlServiceSpy();
    simulation = simulationServiceSpy();

    TestBed.configureTestingModule({
      declarations: [RunSimulationMenuItemComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: treeControl},
        {provide: SimulationService, useValue: simulation},
        {provide: STORAGE_ID, useValue: 'storage'},
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

  it('should run', () => {
    const ctrl = component.treeControl as any;
    ctrl.hasSingleSelection = true;
    ctrl.first = testStorageFileNode();
    simulation.isSimulationNode.and.returnValue(true);
    const binding = component.binding;
    expect(binding.keys).toEqual(['shift + ctrl + X']);
    expect(binding.binding(null)).toBe(true);
    expect(simulation.run).toHaveBeenCalledWith(testStorageFileNode());
  });

  it('should not run', () => {
    const ctrl = component.treeControl as any;
    ctrl.hasSingleSelection = false;
    const binding = component.binding;
    expect(binding.binding(null)).toBe(false);
  });
});
