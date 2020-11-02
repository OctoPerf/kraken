import {TestBed} from '@angular/core/testing';

import {StorageKeyBindingService} from './storage-key-binding.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {
  STORAGE_ROOT_NODE,
  StorageTreeDataSourceService
} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {storageTreeDataSourceServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service.spec';
import {testStorageRootNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {keyBindingsServiceSpy} from 'projects/tools/src/lib/key-bindings.service.spec';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageTreeScrollService} from 'projects/storage/src/lib/storage-tree/storage-tree-scroll.service';
import {storageTreeScrollServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-scroll.service.spec';
import * as _ from 'lodash';
import SpyObj = jasmine.SpyObj;

export const storageKeyBindingServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageKeyBindingService', [
    'init',
    'upSelection'
  ]);
  return spy;
};

describe('StorageKeyBindingService', () => {

  let rootNode: StorageNode;
  let treeControl: SpyObj<StorageTreeControlService>;
  let keys: SpyObj<KeyBindingsService>;
  let dataSource: SpyObj<StorageTreeDataSourceService>;
  let storageService: SpyObj<StorageService>;
  let scroll: SpyObj<StorageTreeScrollService>;

  let service: StorageKeyBindingService;

  beforeEach(() => {
    rootNode = testStorageRootNode();
    keys = keyBindingsServiceSpy();
    dataSource = storageTreeDataSourceServiceSpy();
    dataSource.indexOf.and.callFake(node => _.indexOf(dataSource._expandedNodes, node));
    treeControl = storageTreeControlServiceSpy();
    storageService = storageServiceSpy();
    scroll = storageTreeScrollServiceSpy();

    TestBed.configureTestingModule({
      providers: [
        {provide: StorageTreeDataSourceService, useValue: dataSource},
        {provide: StorageTreeControlService, useValue: treeControl},
        {provide: StorageService, useValue: storageService},
        {provide: STORAGE_ID, useValue: 'test'},
        {provide: STORAGE_ROOT_NODE, useValue: rootNode},
        {provide: KeyBindingsService, useValue: keys},
        {provide: StorageTreeScrollService, useValue: scroll},
        StorageKeyBindingService,
      ]
    });
    service = TestBed.inject(StorageKeyBindingService);
    service.init();
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('should select ', () => {

    beforeEach(() => {
      treeControl._lastSelection = dataSource._expandedNodes[7];
    });

    it('up data', () => {
      expect(service.upSelection()).toBe(true);
      expect(treeControl.selectOne).toHaveBeenCalledTimes(1);
      expect(treeControl.selectOne).toHaveBeenCalledWith(dataSource._expandedNodes[6]);
    });

    it('down data', () => {
      expect(service.downSelection()).toBe(true);
      expect(treeControl.selectOne).toHaveBeenCalledTimes(1);
      expect(treeControl.selectOne).toHaveBeenCalledWith(dataSource._expandedNodes[8]);
    });

  });

  describe('should multi select ', () => {

    beforeEach(() => {
      treeControl._lastSelection = dataSource._expandedNodes[7];
    });

    it('down data', () => {
      expect(service.downMultiSelection()).toBe(true);
      expect(treeControl.selectNode).toHaveBeenCalledTimes(1);
      expect(treeControl.selectNode).toHaveBeenCalledWith(dataSource._expandedNodes[8]);
    });

    it('up data', () => {
      expect(service.upMultiSelection()).toBe(true);
      expect(treeControl.selectNode).toHaveBeenCalledTimes(1);
      expect(treeControl.selectNode.withArgs(dataSource._expandedNodes[6]));
    });

    it('down data with lines already selected', () => {
      treeControl.isSelected.withArgs(dataSource._expandedNodes[8]).and.returnValue(true);
      expect(service.downMultiSelection()).toBe(true);
      expect(treeControl.deselectNode).toHaveBeenCalledTimes(1);
      expect(treeControl.deselectNode).toHaveBeenCalledWith(dataSource._expandedNodes[7], dataSource._expandedNodes[8]);
    });

    it('up data with lines already selected', () => {
      treeControl.isSelected.withArgs(dataSource._expandedNodes[6]).and.returnValue(true);
      expect(service.upMultiSelection()).toBe(true);
      expect(treeControl.deselectNode).toHaveBeenCalledTimes(1);
      expect(treeControl.deselectNode).toHaveBeenCalledWith(dataSource._expandedNodes[7], dataSource._expandedNodes[6]);
    });
  });

  it('should not select up data', () => {
    treeControl._lastSelection = dataSource._expandedNodes[0];
    expect(service.upSelection()).toBe(false);
    expect(treeControl.selectOne).toHaveBeenCalledTimes(0);
  });

  it('should not select down data', () => {
    treeControl._lastSelection = dataSource._expandedNodes[dataSource._expandedNodes.length - 1];
    expect(service.downSelection()).toBe(false);
    expect(treeControl.selectOne).toHaveBeenCalledTimes(0);
  });

  it('should not multi select up data', () => {
    treeControl._lastSelection = dataSource._expandedNodes[0];
    expect(service.upMultiSelection()).toBe(false);
    expect(treeControl.selectOne).toHaveBeenCalledTimes(0);
  });

  it('should not multi select down data', () => {
    treeControl._lastSelection = dataSource._expandedNodes[dataSource._expandedNodes.length - 1];
    expect(service.downMultiSelection()).toBe(false);
    expect(treeControl.selectOne).toHaveBeenCalledTimes(0);
  });

  it('should open data', () => {
    (treeControl as any).selected = [dataSource._expandedNodes[7]];
    expect(service.openSelection()).toBe(true);
    expect(treeControl.nodeDoubleClick).toHaveBeenCalledWith(dataSource._expandedNodes[7]);
  });

  it('should open multi datas', () => {
    (treeControl as any).selected = [dataSource._expandedNodes[7], dataSource._expandedNodes[8]];
    expect(service.openSelection()).toBe(true);
    expect(treeControl.nodeDoubleClick).toHaveBeenCalledTimes(2);
    expect(treeControl.nodeDoubleClick).toHaveBeenCalledWith(dataSource._expandedNodes[7]);
    expect(treeControl.nodeDoubleClick).toHaveBeenCalledWith(dataSource._expandedNodes[8]);
  });

  it('should right selection for directory', () => {
    treeControl._lastSelection = dataSource._expandedNodes[6];
    const downSelection = spyOn(service, 'downSelection');
    treeControl.isExpanded.withArgs(dataSource._expandedNodes[6]).and.returnValue(true);
    treeControl.isExpanded.withArgs(dataSource._expandedNodes[5]).and.returnValue(true);
    treeControl.isExpanded.withArgs(dataSource._expandedNodes[4]).and.returnValue(true);
    dataSource.parentNode.withArgs(dataSource._expandedNodes[7]).and.returnValue(dataSource._expandedNodes[6]);
    service.rightSelection();
    expect(downSelection).toHaveBeenCalledTimes(1);
    expect(treeControl.expand).toHaveBeenCalledTimes(0);
  });

  it('should right selection for directory expanded', () => {
    treeControl._lastSelection = dataSource._expandedNodes[5];
    treeControl.isExpanded.withArgs(dataSource._expandedNodes[5]).and.returnValue(false);
    expect(service.rightSelection()).toBe(true);
    expect(treeControl.expand).toHaveBeenCalledTimes(1);
    expect(treeControl.expand).toHaveBeenCalledWith(dataSource._expandedNodes[5]);
  });

  it('should right selection for file', () => {
    treeControl._lastSelection = dataSource._expandedNodes[7];
    const downSelection = spyOn(service, 'downSelection');
    treeControl.isExpanded.withArgs(dataSource._expandedNodes[6]).and.returnValue(true);
    treeControl.isExpanded.withArgs(dataSource._expandedNodes[7]).and.returnValue(true);
    treeControl.isExpanded.withArgs(dataSource._expandedNodes[8]).and.returnValue(true);
    dataSource.parentNode.withArgs(dataSource._expandedNodes[8]).and.returnValue(dataSource._expandedNodes[6]);
    service.rightSelection();
    expect(downSelection).toHaveBeenCalledTimes(1);
    expect(treeControl.expand).toHaveBeenCalledTimes(0);
  });

  it('should left selection for directory', () => {
    treeControl._lastSelection = dataSource._expandedNodes[5];
    dataSource.parentNode.withArgs(dataSource._expandedNodes[5]).and.returnValue(dataSource._expandedNodes[4]);
    treeControl.isExpanded.withArgs(dataSource._expandedNodes[5]).and.returnValue(false);
    expect(service.leftSelection()).toBe(true);
    expect(treeControl.selectOne).toHaveBeenCalledTimes(1);
    expect(treeControl.selectOne).toHaveBeenCalledWith(dataSource._expandedNodes[4]);
    expect(treeControl.collapse).toHaveBeenCalledTimes(0);
  });

  it('should left selection for directory expanded', () => {
    treeControl._lastSelection = dataSource._expandedNodes[6];
    treeControl.isExpanded.withArgs(dataSource._expandedNodes[6]).and.returnValue(true);
    expect(service.leftSelection()).toBe(true);
    expect(treeControl.collapse).toHaveBeenCalledTimes(1);
    expect(treeControl.collapse).toHaveBeenCalledWith((dataSource._expandedNodes[6]));
    expect(treeControl.selectOne).toHaveBeenCalledTimes(0);
  });

  it('should left selection for file', () => {
    treeControl._lastSelection = dataSource._expandedNodes[7];
    dataSource.parentNode.withArgs(dataSource._expandedNodes[7]).and.returnValue(dataSource._expandedNodes[6]);
    expect(service.leftSelection()).toBe(true);
    expect(treeControl.selectOne).toHaveBeenCalledTimes(1);
    expect(treeControl.selectOne).toHaveBeenCalledWith(dataSource._expandedNodes[6]);
    expect(treeControl.collapse).toHaveBeenCalledTimes(0);
  });

  it('should delete selection', () => {
    (treeControl as any).selected = [dataSource._expandedNodes[7], dataSource._expandedNodes[8]];
    expect(service.deleteSelection(true)).toBe(true);
    expect(storageService.deleteFiles).toHaveBeenCalledWith([dataSource._expandedNodes[7], dataSource._expandedNodes[8]], true);
  });

  it('should left selection like up selection', () => {
    treeControl._lastSelection = dataSource._expandedNodes[12];
    dataSource.parentNode.withArgs(dataSource._expandedNodes[12]).and.returnValue(rootNode);
    expect(service.leftSelection()).toBe(true);
    expect(treeControl.selectOne).toHaveBeenCalledTimes(1);
    expect(treeControl.selectOne).toHaveBeenCalledWith(dataSource._expandedNodes[11]);
    expect(treeControl.collapse).toHaveBeenCalledTimes(0);
  });

});
