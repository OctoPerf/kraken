import {TestBed} from '@angular/core/testing';

import {STORAGE_ROOT_NODE, StorageTreeDataSourceService} from './storage-tree-data-source.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageWatcherService} from 'projects/storage/src/lib/storage-watcher.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {storageWatcherServiceSpy} from 'projects/storage/src/lib/storage-watcher.service.spec';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {storageTreeControlServiceSpy} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service.spec';
import {EMPTY} from 'rxjs';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {getTestScheduler} from 'jasmine-marbles';
import * as _ from 'lodash';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {copyPasteServiceSpy} from 'projects/storage/src/lib/storage-tree/copy-paste.service.spec';
import {testStorageNodes, testStorageRootNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import Spy = jasmine.Spy;
import {StorageNodeToPredicatePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-predicate.pipe';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {storageListServiceSpy} from 'projects/storage/src/lib/storage-list.service.spec';
import SpyObj = jasmine.SpyObj;
import {PathToParentPathPipe} from 'projects/tools/src/lib/path-to-parent-path.pipe';

export const testStorageNodesSorted: () => StorageNode[] = () => {
  return [
    {path: 'reports', type: 'DIRECTORY', depth: 0, length: 0, lastModified: 0},
    {path: 'reports/tests', type: 'DIRECTORY', depth: 1, length: 0, lastModified: 0},
    {path: 'reports/tests/test', type: 'DIRECTORY', depth: 2, length: 0, lastModified: 0},
    {path: 'reports/tests/test/css', type: 'DIRECTORY', depth: 3, length: 0, lastModified: 0},
    {path: 'reports/tests/test/css/base-style.css', type: 'FILE', depth: 4, length: 42, lastModified: 1337},
    {path: 'reports/tests/test/css/style.css', type: 'FILE', depth: 4, length: 42, lastModified: 1337},
    {path: 'reports/tests/test/js', type: 'DIRECTORY', depth: 3, length: 0, lastModified: 0},
    {path: 'reports/tests/test/js/report.js', type: 'FILE', depth: 4, length: 42, lastModified: 1337},
    {path: 'reports/tests/test/index.html', type: 'FILE', depth: 3, length: 42, lastModified: 1337},
    {path: 'spotbugs', type: 'DIRECTORY', depth: 0, length: 0, lastModified: 0},
    {path: 'spotbugs/main.html', type: 'FILE', depth: 1, length: 42, lastModified: 1337},
    {path: 'spotbugs/test.html', type: 'FILE', depth: 1, length: 42, lastModified: 1337},
    {path: 'report.js', type: 'FILE', depth: 0, length: 42, lastModified: 1337},
  ];
};

export const storageTreeDataSourceServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageTreeDataSourceService', [
    'init',
    'connect',
    'disconnect',
    'parentNode',
    'ngOnDestroy',
  ]);
  spy.data = testStorageNodes();
  spy._expandedNodes = testStorageNodes();
  return spy;
};

describe('StorageTreeDataSourceService', () => {
  let service: StorageTreeDataSourceService;

  let storage: SpyObj<StorageService>;
  let storageList: SpyObj<StorageListService>;
  let treeControl: StorageTreeControlService;
  let nodes: StorageNode[];
  let rootNode: StorageNode;

  beforeEach(() => {
    rootNode = testStorageRootNode();

    TestBed.configureTestingModule({
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: StorageListService, useValue: storageListServiceSpy()},
        {provide: STORAGE_ROOT_NODE, useValue: rootNode},
        StorageNodeToParentPathPipe,
        PathToParentPathPipe,
        StorageTreeDataSourceService,
      ]
    });
    storage = TestBed.inject(StorageService);
    storageList = TestBed.inject(StorageListService);
    service = TestBed.inject(StorageTreeDataSourceService);
    treeControl = storageTreeControlServiceSpy();
    nodes = testStorageNodes();
    service.treeControl = treeControl;
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set (and get) data', () => {
    service.data = nodes;
    expect(service.data).toBe(storageList.nodes);
    expect(service._nodesMap[nodes[0].path]).toBe(nodes[0]);
    expect(treeControl.dataNodes).toBe(nodes);
    expect(treeControl.loadExpansion).toHaveBeenCalledWith(nodes);
  });

  it('should connect', () => {
    (treeControl.isExpanded as Spy).and.returnValue(true);
    service.connect({
      viewChange: EMPTY
    }).subscribe(_nodes => expect(_nodes.length).toBe(nodes.length));
  });

  it('should disconnect', () => {
    service.disconnect();
  });

  it('should init', () => {
    const subjectSpy = spyOn(storageList.nodesSubject, 'subscribe').and.callThrough();
    service.init();
    expect(storageList.init).toHaveBeenCalledWith('');
    expect(subjectSpy).toHaveBeenCalled();
  });

  it('should init custom root node', () => {
    (service as any).rootNode.path = 'path';
    const subjectSpy = spyOn(storageList.nodesSubject, 'subscribe').and.callThrough();
    service.init();
    expect(storageList.init).toHaveBeenCalledWith('path/');
    expect(subjectSpy).toHaveBeenCalled();
  });

  it('should parentNode', () => {
    service.data = nodes;
    expect(service.parentNode(nodes[1])).toBe(nodes[0]);
  });

  it('should parentNode root', () => {
    service.data = nodes;
    expect(service.parentNode(nodes[0])).toEqual(rootNode);
  });

});
