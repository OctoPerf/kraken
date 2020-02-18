import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DebugSimulationMenuItemComponent} from 'projects/gatling/src/app/simulations/debug-simulation-menu-item/debug-simulation-menu-item.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import SpyObj = jasmine.SpyObj;

describe('DebugSimulationMenuItemComponent', () => {
  let component: DebugSimulationMenuItemComponent;
  let fixture: ComponentFixture<DebugSimulationMenuItemComponent>;
  let treeControl: SpyObj<StorageTreeControlService>;
  let simulation: SpyObj<SimulationService>;

  beforeEach(async(() => {
    treeControl = storageTreeControlServiceSpy();
    simulation = simulationServiceSpy();

    TestBed.configureTestingModule({
      declarations: [DebugSimulationMenuItemComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: treeControl},
        {provide: SimulationService, useValue: simulation},
        {provide: STORAGE_ID, useValue: 'storage'},
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

  it('should debug', () => {
    const ctrl = component.treeControl as any;
    ctrl.hasSingleSelection = true;
    ctrl.first = testStorageFileNode();
    simulation.isSimulationNode.and.returnValue(true);
    const binding = component.binding;
    expect(binding.keys).toEqual(['shift + ctrl + D']);
    expect(binding.binding(null)).toBe(true);
    expect(simulation.debug).toHaveBeenCalledWith(testStorageFileNode());
  });

  it('should not debug', () => {
    const ctrl = component.treeControl as any;
    ctrl.hasSingleSelection = false;
    const binding = component.binding;
    expect(binding.binding(null)).toBe(false);
  });
});
