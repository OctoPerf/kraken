import {TestBed} from '@angular/core/testing';

import {StorageListService} from './storage-list.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import {StorageNodeToPredicatePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-predicate.pipe';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {testStorageNodes} from 'projects/storage/src/lib/entities/storage-node.spec';
import {testStorageNodesSorted} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service.spec';
import {BehaviorSubject, of} from 'rxjs';
import {NodeCreatedEvent} from 'projects/storage/src/lib/events/node-created-event';
import {NodeModifiedEvent} from 'projects/storage/src/lib/events/node-modified-event';
import * as _ from 'lodash';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {EventEmitter} from '@angular/core';
import {PathToNamePipe} from 'projects/tools/src/lib/path-to-name.pipe';
import {PathToParentPathPipe} from 'projects/tools/src/lib/path-to-parent-path.pipe';
import {sseServiceSpy} from 'projects/sse/src/lib/sse.service.spec';
import {SSEService} from 'projects/sse/src/lib/sse.service';
import SpyObj = jasmine.SpyObj;

export const storageListServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageListService', [
    'init',
    'ngOnDestroy',
  ]);
  const nodes = testStorageNodesSorted();
  spy.nodesSubject = new BehaviorSubject(nodes);
  spy.nodeCreated = new EventEmitter<StorageNode>();
  spy.nodeModified = new EventEmitter<StorageNode>();
  spy.nodesDeleted = new EventEmitter<StorageNode[]>();
  spy.nodesListed = new EventEmitter<StorageNode[]>();
  spy.nodes = nodes;
  return spy;
};


describe('StorageListService', () => {

  let service: StorageListService;
  let storage: SpyObj<StorageService>;
  let eventBus: EventBusService;
  let sseService: SpyObj<SSEService>;

  beforeEach(() => {
    sseService = sseServiceSpy();
    TestBed.configureTestingModule({
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: SSEService, useValue: sseService},
        EventBusService,
        NodeEventToNodePipe,
        StorageNodeToNamePipe,
        PathToNamePipe,
        StorageNodeToParentPathPipe,
        PathToParentPathPipe,
        StorageNodeToPredicatePipe,
        StorageListService,
      ]
    });

    service = TestBed.inject(StorageListService);
    storage = TestBed.inject(StorageService) as SpyObj<StorageService>;
    eventBus = TestBed.inject(EventBusService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set/get nodes', () => {
    service.nodes = testStorageNodes();
    expect(service.nodes).toEqual(testStorageNodesSorted());
  });

  it('should init', () => {
    const subjectSpy = spyOn(service.nodesSubject, 'next').and.callThrough();
    const listedSpy = spyOn(service.nodesListed, 'emit');
    storage.find.and.returnValue(of(testStorageNodes()));
    service.init('rootPath', 'matcher', 42);
    expect(storage.find).toHaveBeenCalledWith('rootPath', 'matcher', 42);
    expect(subjectSpy).toHaveBeenCalledWith(testStorageNodesSorted());
    expect(listedSpy).toHaveBeenCalledWith(testStorageNodesSorted());
  });

  it('should init defaults', () => {
    const subjectSpy = spyOn(service.nodesSubject, 'next').and.callThrough();
    const listedSpy = spyOn(service.nodesListed, 'emit');
    storage.find.and.returnValue(of(testStorageNodes()));
    service.init('');
    expect(storage.find).toHaveBeenCalledWith('', undefined, undefined);
    expect(subjectSpy).toHaveBeenCalledWith(testStorageNodesSorted());
    expect(listedSpy).toHaveBeenCalledWith(testStorageNodesSorted());
  });

  describe('after init', () => {

    let nodes: StorageNode[];
    let node: StorageNode;

    beforeEach(() => {
      nodes = [
        {path: 'reports/report1/report.json', type: 'FILE', depth: 2, length: 42, lastModified: 1337},
        {path: 'reports/report2/report.json', type: 'FILE', depth: 2, length: 42, lastModified: 1337},
      ];
      node = {path: 'reports/report3/report.json', type: 'FILE', depth: 2, length: 42, lastModified: 1337};
      storage.find.and.returnValue(of(nodes));
      service.init('reports', 'report\.json', 42);
    });

    it('should handle node created', () => {
      const createdSpy = spyOn(service.nodeCreated, 'emit');
      eventBus.publish(new NodeCreatedEvent(node));
      expect(createdSpy).toHaveBeenCalledWith(node);
      expect(service.nodes.length).toBe(nodes.length + 1);
    });

    it('should handle existing node created', () => {
      const createdSpy = spyOn(service.nodeCreated, 'emit');
      const modifiedSpy = spyOn(service.nodeModified, 'emit');
      const created: StorageNode = {
        path: 'reports/report1/report.json',
        type: 'FILE',
        depth: 2,
        length: 420,
        lastModified: 9999
      };
      eventBus.publish(new NodeCreatedEvent(created));
      expect(modifiedSpy).toHaveBeenCalledWith(created);
      expect(createdSpy).not.toHaveBeenCalled();
      expect(service.nodes.length).toBe(nodes.length);
    });

    it('should filter node created', () => {
      const createdSpy = spyOn(service.nodeCreated, 'emit');
      node.path = 'otherPath';
      eventBus.publish(new NodeCreatedEvent(node));
      expect(createdSpy).not.toHaveBeenCalled();
    });

    it('should filter node created (match)', () => {
      const createdSpy = spyOn(service.nodeCreated, 'emit');
      node.path = 'reports/report3/report.css';
      eventBus.publish(new NodeCreatedEvent(node));
      expect(createdSpy).not.toHaveBeenCalled();
    });

    it('should handle node modified', () => {
      const modifiedSpy = spyOn(service.nodeModified, 'emit');
      const _node = _.cloneDeep(nodes[0]);
      _node.length = 666;
      _node.lastModified = 669;
      eventBus.publish(new NodeModifiedEvent(_node));
      expect(modifiedSpy).toHaveBeenCalledWith(_node);
    });

    it('should handle node deleted', () => {
      const deletedSpy = spyOn(service.nodesDeleted, 'emit');
      eventBus.publish(new NodeDeletedEvent(nodes[0]));
      expect(deletedSpy).toHaveBeenCalledWith([nodes[0]]);
    });

    it('should skip other node deleted', () => {
      const deletedSpy = spyOn(service.nodesDeleted, 'emit');
      eventBus.publish(new NodeDeletedEvent(node));
      expect(deletedSpy).not.toHaveBeenCalled();
    });
  });

});
