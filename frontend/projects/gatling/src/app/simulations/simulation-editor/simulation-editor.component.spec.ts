import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SimulationEditorComponent} from './simulation-editor.component';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {storageNodeEditorContentServiceSpy} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service.spec';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';

describe('SimulationEditorComponent', () => {
  let component: SimulationEditorComponent;
  let fixture: ComponentFixture<SimulationEditorComponent>;

  beforeEach(async(() => {
    const contentService = storageNodeEditorContentServiceSpy();

    TestBed.configureTestingModule({
      declarations: [SimulationEditorComponent],
      providers: [
        {provide: StorageNodeEditorContentService, useValue: contentService},
        {provide: SimulationService, useValue: simulationServiceSpy()},
        {provide: STORAGE_NODE, useValue: testStorageFileNode()},
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
});
