import {TestBed} from '@angular/core/testing';

import {StorageTreeScrollService} from './storage-tree-scroll.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {storageTreeDataSourceServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service.spec';
import SpyObj = jasmine.SpyObj;
import {CdkVirtualScrollViewport} from '@angular/cdk/scrolling';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';

export const storageTreeScrollServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageTreeScrollService', [
    'init',
    'updateScroll',
  ]);
  return spy;
};

describe('StorageTreeScrollService', () => {
  let service: StorageTreeScrollService;
  let treeControl: SpyObj<StorageTreeControlService>;
  let treeData: SpyObj<StorageTreeDataSourceService>;

  beforeEach(() => {
    treeControl = storageTreeControlServiceSpy();
    treeData = storageTreeDataSourceServiceSpy();

    TestBed.configureTestingModule({
      providers: [
        {provide: StorageTreeControlService, useValue: treeControl},
        {provide: StorageTreeDataSourceService, useValue: treeData},
        StorageTreeScrollService
      ]
    });
    service = TestBed.inject(StorageTreeScrollService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should update scroll', () => {
    const spy = jasmine.createSpyObj('CdkVirtualScrollViewport', ['scrollToIndex']);
    const node = testStorageFileNode();
    treeControl._lastSelection = node;
    treeData.findIndex.and.returnValue(42);
    service.init(spy);
    service.updateScroll();
    expect(spy.scrollToIndex).toHaveBeenCalledWith(42);
    expect(treeData.findIndex).toHaveBeenCalledWith(node);
  });

  it('should not update scroll', () => {
    const spy = jasmine.createSpyObj('CdkVirtualScrollViewport', ['scrollToIndex']);
    const node = testStorageFileNode();
    treeControl._lastSelection = null;
    treeData.findIndex.and.returnValue(42);
    service.init(spy);
    service.updateScroll();
    expect(spy.scrollToIndex).not.toHaveBeenCalled();
    expect(treeData.findIndex).not.toHaveBeenCalled();
  });
});
