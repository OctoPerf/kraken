import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {StorageJsonService} from './storage-json.service';
import {Injectable, OnDestroy} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {testStorageDirectoryNode, testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {storageListServiceSpy} from 'projects/storage/src/lib/storage-list.service.spec';
import SpyObj = jasmine.SpyObj;
import {of} from 'rxjs';
import * as _ from 'lodash';

export class TestValue {
  id: string;
}

@Injectable()
export class TestStorageJsonService extends StorageJsonService<TestValue> implements OnDestroy {

  constructor(
    storage: StorageService,
    storageList: StorageListService,
  ) {
    super(storage, storageList, node => node.path, value => value.id);
    super.init('rootPath', 'matcher', 42);
  }

  ngOnDestroy(): void {
    super.clearSubscriptions();
  }
}

describe('StorageJsonService', () => {
  let service: TestStorageJsonService;
  let storage: SpyObj<StorageService>;
  let storageList: SpyObj<StorageListService>;

  let node: StorageNode;
  let value: TestValue;

  beforeEach(() => {
    node = testStorageFileNode();
    value = {id: node.path};
    storage = storageServiceSpy();
    storageList = storageListServiceSpy();

    TestBed.configureTestingModule({
      providers: [
        {provide: StorageService, useValue: storage},
        {provide: StorageListService, useValue: storageList},
        TestStorageJsonService,
      ]
    });
    service = TestBed.inject(TestStorageJsonService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    expect(storageList.init).toHaveBeenCalledWith('rootPath', 'matcher', 42);
  });

  it('should set/get values', () => {
    const values = [value];
    service.values = values;
    expect(service.values).toBe(values);
  });

  it('should find', () => {
    service.values = [value];
    expect(service.find(node)).toBe(value);
  });

  it('should _nodesListed', fakeAsync(() => {
    const nodes = [node];
    const values = [value];
    storage.listJSON.and.returnValue(of(values));
    storageList.nodesListed.emit(nodes);
    tick(500);
    expect(service.values).toBe(values);
    expect(service.loading).toBeFalse();
  }));

  it('should _nodeCreated', () => {
    const values = [value];
    storage.getJSON.and.returnValue(of(value));
    storageList.nodeCreated.emit(node);
    expect(service.values).toEqual(values);
  });

  it('should _nodeCreated no duplicates', () => {
    const values = [value];
    service.values = values;
    storage.getJSON.and.returnValue(of(value));
    storageList.nodeCreated.emit(node);
    expect(service.values.length).toBe(1);
  });


  it('should _nodesDeleted', () => {
    const values = [value];
    service.values = values;
    storageList.nodesDeleted.emit([node]);
    expect(service.values).toEqual([]);
  });

  it('should _nodesDeleted nope', () => {
    const values = [value];
    service.values = values;
    storageList.nodesDeleted.emit([testStorageDirectoryNode()]);
    expect(service.values).toBe(values);
  });

  it('should _nodeModified', () => {
    service.values = [value];
    const modified = _.cloneDeep(node);
    modified.length = 666;
    modified.lastModified = 666;
    const modifiedValue = {id: modified.path};
    storage.getJSON.and.returnValue(of(modifiedValue));
    storageList.nodeModified.emit(modified);
    expect(service.values[0]).toBe(modifiedValue);
  });

  it('should _nodeModified nope', () => {
    service.values = [value];
    storageList.nodeModified.emit(testStorageDirectoryNode());
    expect(service.values[0]).toBe(value);
  });

});
