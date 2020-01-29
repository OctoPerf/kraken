import {TestBed} from '@angular/core/testing';

import {StorageTreeControlService} from './storage-tree-control.service';
import {SelectionModel} from '@angular/cdk/collections';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {eventSpy} from 'projects/commons/src/lib/mock/event.mock.spec';
import {
  STORAGE_ROOT_NODE,
  StorageTreeDataSourceService
} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {storageTreeDataSourceServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service.spec';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {OpenNodeEvent} from 'projects/storage/src/lib/events/open-node-event';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import * as _ from 'lodash';
import {
  testStorageDirectoryNode,
  testStorageFileNode,
  testStorageNodes,
  testStorageRootNode
} from 'projects/storage/src/lib/entities/storage-node.spec';
import SpyObj = jasmine.SpyObj;
import Spy = jasmine.Spy;
import {StorageNodeToPredicatePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-predicate.pipe';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {storageListServiceSpy} from 'projects/storage/src/lib/storage-list.service.spec';


export const storageTreeControlServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageTreeControlService', [
    'nodeClick',
    'mouseNodeDoubleClick',
    'nodeDoubleClick',
    'nodeContextMenu',
    'clearSelection',
    'clearExpansion',
    'isSelected',
    'isExpanded',
    'expand',
    'collapse',
    'selectOne',
    'selectNode',
    'deselectNode',
    'loadExpansion',
    'ngOnDestroy',
  ]);
  spy.expansionModel = new SelectionModel<StorageNode>();
  spy._selection = new SelectionModel<StorageNode>();
  return spy;
};

describe('StorageTreeControlService', () => {

  let service: StorageTreeControlService;
  let selection: SpyObj<SelectionModel<StorageNode>>;
  let eventBus: EventBusService;
  let dataSource: StorageTreeDataSourceService;
  let localStorage: LocalStorageService;
  let fileNode: StorageNode;
  let rootNode: StorageNode;
  let directoryNode: StorageNode;
  let storageNodes: StorageNode[];

  beforeEach(() => {
    selection = jasmine.createSpyObj('_selection', ['clear', 'select', 'toggle', 'isSelected', 'hasValue']);

    rootNode = testStorageRootNode();

    TestBed.configureTestingModule({
      providers: [
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: StorageTreeDataSourceService, useValue: storageTreeDataSourceServiceSpy()},
        {provide: StorageListService, useValue: storageListServiceSpy()},
        {provide: LocalStorageService, useValue: localStorageServiceSpy()},
        {provide: STORAGE_ID, useValue: 'test'},
        {provide: STORAGE_ROOT_NODE, useValue: rootNode},
        StorageTreeControlService,
        StorageNodeToPredicatePipe,
      ]
    });
    eventBus = TestBed.get(EventBusService);
    dataSource = TestBed.get(StorageTreeDataSourceService);
    localStorage = TestBed.get(LocalStorageService);
    service = TestBed.get(StorageTreeControlService);
    service._selection = selection as any;
    fileNode = testStorageFileNode();
    directoryNode = testStorageDirectoryNode();
    storageNodes = testStorageNodes();
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    service.ngOnDestroy();
  });

  it('should toggle _selection no key', () => {
    spyOn(service, 'selectOne');
    service._toggleSelection({ctrlKey: false, shiftKey: false} as any, directoryNode);
    expect(service.selectOne).toHaveBeenCalledWith(directoryNode);
  });

  it('should toggle _selection shift key no last _selection', () => {
    spyOn(service, 'selectOne');
    service._toggleSelection({ctrlKey: false, shiftKey: true} as any, directoryNode);
    expect(service.selectOne).toHaveBeenCalledWith(directoryNode);
  });

  it('should toggle _selection ctrl key', () => {
    (selection as any).selected = [directoryNode];
    service._toggleSelection({ctrlKey: true, shiftKey: false} as any, directoryNode);
    expect(selection.toggle).toHaveBeenCalledWith(directoryNode);
    expect(service._lastSelection).toBe(directoryNode);
  });

  it('should toggle _selection shift key', () => {
    service._lastSelection = directoryNode;
    const nodes: StorageNode[] = [
      directoryNode,
      {
        path: 'root/path/file1.css',
        type: 'FILE',
        depth: 2, length: 42, lastModified: 1337
      },
      {
        path: 'root/path/file2.css',
        type: 'FILE',
        depth: 2, length: 42, lastModified: 1337
      },
    ];
    dataSource.data = nodes;
    service._toggleSelection({ctrlKey: false, shiftKey: true} as any, nodes[2]);
    expect(selection.clear).toHaveBeenCalled();
    expect(selection.select).toHaveBeenCalledTimes(3);
    expect(service._lastSelection).toBe(directoryNode);
  });

  it('should handle tree click', () => {
    service.clearSelection();
    expect(selection.clear).toHaveBeenCalled();
    expect(service._lastSelection).toBeNull();
  });

  it('should return is selected', () => {
    (selection as any).selected = [directoryNode];
    expect(service.isSelected(directoryNode)).toBe(true);
    expect(service.isSelected(fileNode)).toBe(false);
    expect(service.isSelected(undefined)).toBe(false);
  });

  it('should handle double click on file', () => {
    const node = fileNode;
    const event = eventSpy();
    selection.isSelected.and.returnValue(true);
    service.mouseNodeDoubleClick(event, node);
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(eventBus.publish).toHaveBeenCalledWith(new OpenNodeEvent(node));
  });

  it('should handle double click on directory', () => {
    const node = directoryNode;
    const event = eventSpy();
    spyOn(service, 'toggle');
    selection.isSelected.and.returnValue(true);
    service.mouseNodeDoubleClick(event, node);
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(service.toggle).toHaveBeenCalledWith(node);
  });

  it('should handle click toggle _selection', () => {
    const node = directoryNode;
    const event = eventSpy();
    spyOn(service, '_toggleSelection');
    service.nodeClick(event, node);
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(service._toggleSelection).toHaveBeenCalledWith(event, node);
  });

  it('should handle context menu', () => {
    service._lastSelection = directoryNode;
    const node = fileNode;
    const event = eventSpy();
    (selection.isSelected).and.returnValue(false);
    service.nodeContextMenu(event, node);
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(selection.clear).toHaveBeenCalled();
    expect(service._lastSelection).toBe(node);
    expect(selection.select).toHaveBeenCalledWith(node);
  });

  it('should handle context menu selected node', () => {
    service._lastSelection = directoryNode;
    const node = fileNode;
    const event = eventSpy();
    spyOn(service, 'isSelected').and.returnValue(true);
    service.nodeContextMenu(event, node);
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(selection.clear).not.toHaveBeenCalled();
    expect(service._lastSelection).not.toBe(node);
    expect(selection.select).toHaveBeenCalledWith(node);
  });

  it('should handle context menu null', () => {
    const event = eventSpy();
    (selection.isSelected).and.returnValue(false);
    service.nodeContextMenu(event, null);
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(selection.clear).toHaveBeenCalled();
    expect(service._lastSelection).toBeNull();
    expect(selection.select).not.toHaveBeenCalled();
  });

  it('should return selected', () => {
    const selected = [directoryNode];
    service._selection = {selected} as any;
    expect(service.selected).toBe(selected);
  });

  it('should return has _selection', () => {
    (selection.hasValue).and.returnValue(true);
    expect(service.hasSelection).toBe(true);
  });

  it('should return has _selection', () => {
    (selection.hasValue).and.returnValue(true);
    expect(service.hasSelection).toBe(true);
  });

  it('should return has single or no _selection', () => {
    service._selection = {selected: [fileNode]} as any;
    expect(service.hasSingleOrNoSelection).toBe(true);
    service._selection = {selected: []} as any;
    expect(service.hasSingleOrNoSelection).toBe(true);
  });

  it('should return has single _selection', () => {
    const selected = [fileNode];
    service._selection = {selected} as any;
    expect(service.hasSingleSelection).toBe(true);
    expect(service.hasMultiSelection).toBe(false);
  });

  it('should return has multi _selection', () => {
    const selected = [fileNode, directoryNode];
    service._selection = {selected} as any;
    expect(service.hasSingleSelection).toBe(false);
    expect(service.hasMultiSelection).toBe(true);
  });

  it('should return has single file _selection', () => {
    const selected = [fileNode];
    service._selection = {selected} as any;
    expect(service.hasSingleFileSelection).toBe(true);
    expect(service.hasSingleDirectorySelection).toBe(false);
  });

  it('should return has single directory _selection', () => {
    const selected = [directoryNode];
    service._selection = {selected} as any;
    expect(service.hasSingleFileSelection).toBe(false);
    expect(service.hasSingleDirectorySelection).toBe(true);
  });

  it('should return empty _selection', () => {
    const selected = [];
    service._selection = {selected} as any;
    expect(service.hasSingleSelection).toBe(false);
    expect(service.hasSingleFileSelection).toBe(false);
    expect(service.hasSingleDirectorySelection).toBe(false);
  });

  it('should return selected directory no _selection', () => {
    selection.hasValue.and.returnValue(false);
    expect(service.selectedDirectory).toEqual(rootNode);
  });

  it('should return selected directory first dir', () => {
    (selection as any).selected = [directoryNode];
    selection.hasValue.and.returnValue(true);
    expect(service.selectedDirectory).toBe(directoryNode);
  });

  it('should return selected directory parent dir', () => {
    (selection as any).selected = [fileNode];
    selection.hasValue.and.returnValue(true);
    const nodes = [
      directoryNode,
      fileNode,
    ];
    dataSource.data = nodes;
    expect(service.selectedDirectory).toBe(directoryNode);
  });

  it('should return selected directory parent root dir', () => {
    const rootFile: StorageNode = {
      path: 'file.ts',
      type: 'FILE',
      depth: 0, length: 42, lastModified: 1337
    };
    (selection as any).selected = [rootFile];
    selection.hasValue.and.returnValue(true);
    const nodes: StorageNode[] = [
      directoryNode,
      fileNode,
      rootFile,
    ];
    dataSource.data = nodes;
    expect(service.selectedDirectory).toEqual(rootNode);
  });

  it('should remove deleted files from selection', () => {
    service._selection = new SelectionModel<StorageNode>(true);
    service._lastSelection = fileNode;
    service._selection.select(fileNode);
    service._nodeDeleted(new NodeDeletedEvent(fileNode));
    expect(service._lastSelection).toBeNull();
    expect(service._selection.hasValue()).toBeFalsy();
  });

  it('should loadExpansion', () => {
    (localStorage.getItem as Spy).and.returnValue(_.cloneDeep([storageNodes[0], storageNodes[1]]));
    service.loadExpansion(storageNodes);
    expect(service.expansionModel.selected.length).toBe(2);
    expect(service.expansionModel.selected[0]).toBe(storageNodes[0]);
    expect(service.expansionModel.selected[1]).toBe(storageNodes[1]);
  });

  it('should clearExpansion', () => {
    spyOn(service.expansionModel, 'clear');
    service.clearExpansion();
    expect(service.expansionModel.clear).toHaveBeenCalled();
  });

  it('should selectOne', () => {
    (dataSource.parentNode as Spy).and.returnValues(directoryNode, rootNode);
    service.selectOne(fileNode);
    expect(selection.clear).toHaveBeenCalled();
    expect(selection.select).toHaveBeenCalledWith(fileNode);
    expect(service._lastSelection).toBe(fileNode);
    expect(service.expansionModel.isSelected(directoryNode)).toBeTruthy();
  });

});
