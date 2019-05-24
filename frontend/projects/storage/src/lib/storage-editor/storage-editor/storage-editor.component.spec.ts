import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {StorageEditorComponent} from './storage-editor.component';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import * as _ from 'lodash';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';
import {OpenNodeEvent} from 'projects/storage/src/lib/events/open-node-event';
import {StorageEditorService} from 'projects/storage/src/lib/storage-editor/storage-editor.service';
import {storageEditorServiceSpy} from 'projects/storage/src/lib/storage-editor/storage-editor.service.spec';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {SaveNodeEvent} from 'projects/storage/src/lib/events/save-node-event';
import Spy = jasmine.Spy;
import {NodePendingSaveEvent} from 'projects/storage/src/lib/events/node-pending-save-event';
import {testStorageFileNode, testStorageNodes} from 'projects/storage/src/lib/entities/storage-node.spec';
import {StorageNodeToPredicatePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-predicate.pipe';
import SpyObj = jasmine.SpyObj;
import {cold, getTestScheduler} from 'jasmine-marbles';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {storageConfigurationServiceSpy} from 'projects/storage/src/lib/storage-configuration.service.spec';

describe('StorageEditorComponent', () => {
  let component: StorageEditorComponent;
  let fixture: ComponentFixture<StorageEditorComponent>;
  let storage: SpyObj<StorageService>;
  let eventBus: EventBusService;
  let editorService: StorageEditorService;
  let configuration: SpyObj<StorageConfigurationService>;
  let nodes: StorageNode[];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: STORAGE_ID, useValue: 'test'},
        {provide: LocalStorageService, useValue: localStorageServiceSpy()},
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: StorageEditorService, useValue: storageEditorServiceSpy()},
        {provide: StorageConfigurationService, useValue: storageConfigurationServiceSpy()},
        StorageNodeToPredicatePipe,
      ],
      declarations: [StorageEditorComponent]
    })
      .overrideTemplate(StorageEditorComponent, '')
      .compileComponents();

    nodes = testStorageNodes();
    storage = TestBed.get(StorageService);
    storage.filterExisting.and.returnValue(cold('---x|', {x: nodes}));
    eventBus = TestBed.get(EventBusService);
    editorService = TestBed.get(StorageEditorService);
    configuration = TestBed.get(StorageConfigurationService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StorageEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should filter existing nodes', () => {
    component.nodes.next([]);
    component.selectedIndex.next(42);
    getTestScheduler().flush();
    expect(component.nodes.value).toEqual(nodes);
    expect(component.selectedIndex.value).toBe(nodes.length - 1);
  });

  it('should filter existing nodes and select readme', () => {
    nodes.length = 0;
    component.nodes.next([]);
    component.selectedIndex.next(42);
    getTestScheduler().flush();
    expect(component.nodes.value).toEqual([configuration.readmeNode]);
    expect(component.selectedIndex.value).toBe(0);
  });

  it('should close node', () => {
    component.nodes.next(_.cloneDeep(nodes));
    component.selectedIndex.next(2);
    component.closeNode(4);
    expect(component.nodes.value.length).toBe(nodes.length - 1);
    expect(component.selectedIndex.value).toBe(2);
  });

  it('should close selected node', () => {
    component.nodes.next(_.cloneDeep(nodes));
    component.selectedIndex.next(2);
    component.closeNode(2);
    expect(component.nodes.value.length).toBe(nodes.length - 1);
    expect(component.selectedIndex.value).toBe(2);
  });

  it('should close selected node and update index', () => {
    component.nodes.next(_.cloneDeep(nodes));
    const index = nodes.length - 1;
    component.selectedIndex.next(index);
    component.closeNode(index);
    expect(component.nodes.value.length).toBe(nodes.length - 1);
    expect(component.selectedIndex.value).toBe(index - 1);
  });

  it('should selectIndex', () => {
    component.nodes.next(nodes);
    component.selectedIndex.next(0);
    component.selectIndex(2);
    expect(eventBus.publish).toHaveBeenCalledWith(new SelectNodeEvent(nodes[2]));
  });

  it('should _openNode select tab', () => {
    component.nodes.next(nodes);
    component.selectedIndex.next(0);
    component._openNode(new OpenNodeEvent(nodes[5]));
    expect(component.selectedIndex.value).toBe(5);
  });

  it('should _openNode add tab', () => {
    const node = testStorageFileNode();
    component.nodes.next([]);
    component.selectedIndex.next(-1);
    component._openNode(new OpenNodeEvent(node));
    expect(component.nodes.value).toEqual([node]);
    expect(component.selectedIndex.value).toBe(0);
  });

  it('should return node editor', () => {
    const node = testStorageFileNode();
    const editor: any = {key: 'value'};
    (editorService.getNodeEditor as Spy).and.returnValue(editor);
    expect(component.getNodeEditor(node)).toBe(editor);
    expect(component.getNodeEditor(node)).toBe(editor);
    expect(editorService.getNodeEditor).toHaveBeenCalledTimes(1);
  });

  it('should close deleted node', () => {
    spyOn(component, 'closeNode');
    component.nodes.next(nodes);
    component._closeDeletedNode(new NodeDeletedEvent(nodes[1]));
    expect(component.closeNode).toHaveBeenCalledWith(1);
  });

  it('should not close deleted node', () => {
    spyOn(component, 'closeNode');
    component.nodes.next(nodes);
    component._closeDeletedNode(new NodeDeletedEvent({
      path: 'other/file/that/wont/be/found',
      type: 'DIRECTORY',
      depth: 0,
      length: 0,
      lastModified: 0
    }));
    expect(component.closeNode).not.toHaveBeenCalled();
  });

  it('should save on ctrl + s', () => {
    const binding = component._saveKeyBinding;
    component.nodes.next(nodes);
    component.selectedIndex.next(2);
    expect(binding.binding(null)).toBe(true);
    expect(eventBus.publish).toHaveBeenCalledWith(new SaveNodeEvent(nodes[2]));
  });

  it('should not save on ctrl + s if no nodes', () => {
    const binding = component._saveKeyBinding;
    component.nodes.next([]);
    component.selectedIndex.next(0);
    expect(binding.binding(null)).toBe(true);
    expect(eventBus.publish).not.toHaveBeenCalled();
  });

  it('should handle pending save state', () => {
    const node = testStorageFileNode();
    expect(component.isPendingSave(node)).toBeFalsy();
    component._tagPendingSaveNode(new NodePendingSaveEvent(node, true));
    expect(component.isPendingSave(node)).toBeTruthy();
    component._tagPendingSaveNode(new NodePendingSaveEvent(node, false));
    expect(component.isPendingSave(node)).toBeFalsy();
  });

  it('should close others', () => {
    component.nodes.next(nodes);
    component.selectedIndex.next(1);
    component.closeOthers();
    expect(component.nodes.value.length).toBe(1);
    expect(component.selectedIndex.value).toBe(0);
  });

  it('should close all', () => {
    component.nodes.next(nodes);
    component.selectedIndex.next(1);
    component.closeAll();
    expect(component.nodes.value.length).toBe(0);
    expect(component.selectedIndex.value).toBe(-1);
  });
});
