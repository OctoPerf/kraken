import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SimulationEditorComponent} from './simulation-editor.component';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {storageNodeEditorContentServiceSpy} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service.spec';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import SpyObj = jasmine.SpyObj;
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

describe('SimulationEditorComponent', () => {
  let component: SimulationEditorComponent;
  let fixture: ComponentFixture<SimulationEditorComponent>;
  let simulation: SpyObj<SimulationService>;
  let node: StorageNode;

  beforeEach(async(() => {
    const contentService = storageNodeEditorContentServiceSpy();
    simulation = simulationServiceSpy();
    node = testStorageFileNode();

    TestBed.configureTestingModule({
      declarations: [SimulationEditorComponent],
      providers: [
        {provide: StorageNodeEditorContentService, useValue: contentService},
        {provide: SimulationService, useValue: simulation},
        {provide: STORAGE_NODE, useValue: node},
      ]
    })
      .overrideTemplate(SimulationEditorComponent, '')
      .overrideProvider(StorageNodeEditorContentService, {useValue: contentService})
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SimulationEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run selection', () => {
    expect(component.runSelection()).toBeTrue();
    expect(simulation.run).toHaveBeenCalledWith(node);
  });

  it('should debug selection', () => {
    expect(component.debugSelection()).toBeTrue();
    expect(simulation.debug).toHaveBeenCalledWith(node);
  });
});
