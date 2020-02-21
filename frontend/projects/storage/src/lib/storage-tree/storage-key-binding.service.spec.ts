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
import {StorageNodeComponent} from 'projects/storage/src/lib/storage-tree/storage-node/storage-node.component';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import SpyObj = jasmine.SpyObj;
import {GuiToolsService} from 'projects/tools/src/lib/gui-tools.service';
import {guiToolsServiceSpy} from 'projects/tools/src/lib/gui-tools.service.spec';

export const storageKeyBindingServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageKeyBindingService', [
    'init',
    'upSelection'
  ]);
  return spy;
};

const scrollableTreeSpy = () => {
  const spy = jasmine.createSpyObj('ElementRef', ['']);
  spy.nativeElement = '<div>' +
    '  <div id="username" >Hello</div>' +
    '  <button id="button" />' +
    '</div>';
  return spy;
};

export const element = () => {
  const spy = jasmine.createSpyObj('StorageNodeComponent', [
    'ref'
  ]);
  spy.ref.nativeElement = '<div>' +
    '  <div id="username" >Hello</div>' +
    '  <button id="button" />' +
    '</div>';
  return spy;
};

export const treeChildren = () => {
  const spy = jasmine.createSpyObj('QueryList', [
    'filter'
  ]);
  return spy;
};

describe('StorageKeyBindingService', () => {

  let treeControl: SpyObj<StorageTreeControlService>;
  let rootNode: StorageNode;
  let keys: KeyBindingsService;
  let dataSource: SpyObj<StorageTreeDataSourceService>;
  let storageService: SpyObj<StorageService>;

  let service: StorageKeyBindingService;

  beforeEach(() => {
    rootNode = testStorageRootNode();

    TestBed.configureTestingModule({
      providers: [
        {provide: StorageTreeDataSourceService, useValue: storageTreeDataSourceServiceSpy()},
        {provide: StorageTreeControlService, useValue: storageTreeControlServiceSpy()},
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: STORAGE_ID, useValue: 'test'},
        {provide: STORAGE_ROOT_NODE, useValue: rootNode},
        {provide: KeyBindingsService, useValue: keyBindingsServiceSpy()},
        {provide: StorageNodeComponent, useValue: element()},
        {provide: GuiToolsService, useValue: guiToolsServiceSpy()},
        StorageKeyBindingService,
      ]
    });
    dataSource = TestBed.inject(StorageTreeDataSourceService) as SpyObj<StorageTreeDataSourceService>;
    keys = TestBed.inject(KeyBindingsService);
    treeControl = TestBed.inject(StorageTreeControlService) as SpyObj<StorageTreeControlService>;
    storageService = TestBed.inject(StorageService) as SpyObj<StorageService>;
    service = TestBed.inject(StorageKeyBindingService);
    const treeNodes = treeChildren();
    // const elementObj = TestBed.inject(element());
    service.init(treeNodes, scrollableTreeSpy());

    treeNodes.filter.and.returnValue([element()]);
    // elementObj.ref.nativeElement.offsetTop.and.returnValue(60);
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

    it('should multi select down data', () => {
      expect(service.downMultiSelection()).toBe(true);
      expect(treeControl.selectNode).toHaveBeenCalledTimes(1);
      expect(treeControl.selectNode).toHaveBeenCalledWith(dataSource._expandedNodes[8]);
    });

    it('should multi select up data', () => {
      expect(service.upMultiSelection()).toBe(true);
      expect(treeControl.selectNode).toHaveBeenCalledTimes(1);
      expect(treeControl.selectNode.withArgs(dataSource._expandedNodes[6]));
    });

    it('should multi select down data with lines already selected', () => {
      treeControl.isSelected.withArgs(dataSource._expandedNodes[8]).and.returnValue(true);
      expect(service.downMultiSelection()).toBe(true);
      expect(treeControl.deselectNode).toHaveBeenCalledTimes(1);
      expect(treeControl.deselectNode).toHaveBeenCalledWith(dataSource._expandedNodes[7], dataSource._expandedNodes[8]);
    });

    it('should multi select up data with lines already selected', () => {
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
