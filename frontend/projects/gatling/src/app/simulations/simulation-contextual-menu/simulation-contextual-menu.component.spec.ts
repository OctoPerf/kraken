import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {SimulationContextualMenuComponent} from './simulation-contextual-menu.component';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import SpyObj = jasmine.SpyObj;

describe('SimulationContextualMenuComponent', () => {
  let component: SimulationContextualMenuComponent;
  let fixture: ComponentFixture<SimulationContextualMenuComponent>;
  let treeControl: SpyObj<StorageTreeControlService>;
  let simulation: SpyObj<SimulationService>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SimulationContextualMenuComponent],
      providers: [
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: SimulationService, useValue: simulationServiceSpy()},
      ]
    })
      .overrideTemplate(SimulationContextualMenuComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SimulationContextualMenuComponent);
    component = fixture.componentInstance;
    treeControl = component.treeControl as any;
    simulation = component.simulation as any;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not showDivider', () => {
    (component.treeControl as any).hasSingleSelection = false;
    (component.treeControl as any).hasSelection = true;
    expect(component.showDivider).toBeFalsy();
  });

  it('should showDivider no selection', () => {
    (treeControl as any).hasSingleSelection = false;
    (treeControl as any).hasSelection = false;
    expect(component.showDivider).toBeTruthy();
  });

  it('should showDivider HAR selection', () => {
    (treeControl as any).fisrt = testStorageFileNode();
    (treeControl as any).hasSingleSelection = true;
    (treeControl as any).hasSelection = false;
    simulation.isHarNode.and.returnValue(true);
    simulation.isSimulationNode.and.returnValue(false);
    expect(component.showDivider).toBeTruthy();
  });

  it('should showDivider Simulation selection', () => {
    (treeControl as any).fisrt = testStorageFileNode();
    (treeControl as any).hasSingleSelection = true;
    (treeControl as any).hasSelection = false;
    simulation.isHarNode.and.returnValue(false);
    simulation.isSimulationNode.and.returnValue(true);
    expect(component.showDivider).toBeTruthy();
  });
});
