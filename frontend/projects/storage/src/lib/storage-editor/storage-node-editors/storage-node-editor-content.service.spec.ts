import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {StorageNodeEditorContentService} from './storage-node-editor-content.service';
import {EventEmitter} from '@angular/core';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {NodePendingSaveEvent} from 'projects/storage/src/lib/events/node-pending-save-event';
import {NodeModifiedEvent} from 'projects/storage/src/lib/events/node-modified-event';
import * as _ from 'lodash';
import {SaveNodeEvent} from 'projects/storage/src/lib/events/save-node-event';
import Spy = jasmine.Spy;
import {testStorageDirectoryNode, testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import SpyObj = jasmine.SpyObj;
import {of, throwError} from 'rxjs';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {storageConfigurationServiceSpy} from 'projects/storage/src/lib/storage-configuration.service.spec';

export const storageNodeEditorContentServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageNodeEditorContentService', [
    'load',
    'ngOnDestroy',
  ]);
  spy.contentChanged = new EventEmitter<string>();
  spy.content = '';
  return spy;
};

describe('StorageNodeEditorContentService', () => {

  let service: StorageNodeEditorContentService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;
  let publish: Spy;
  let storage: SpyObj<StorageService>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: StorageConfigurationService, useValue: storageConfigurationServiceSpy()},
        {provide: StorageService, useValue: storageServiceSpy()},
        StorageNodeEditorContentService,
      ]
    });
    service = TestBed.get(StorageNodeEditorContentService);
    storage = TestBed.get(StorageService);
    httpTestingController = TestBed.get(HttpTestingController);
    eventBus = TestBed.get(EventBusService);
    publish = spyOn(eventBus, 'publish');
    publish.and.callThrough();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    service.ngOnDestroy();
  });

  it('should set and get content', () => {
    service.content = 'content';
    expect(service.content).toBe('content');
  });

  it('should load node', () => {
    const node = testStorageFileNode();
    storage.getContent.and.returnValue(of('content'));
    service.load(node);
    expect(service.state).toBe('loaded');
    expect(service.content).toBe('content');
  });

  it('should fail to load node', () => {
    const node = testStorageFileNode();
    storage.getContent.and.returnValue(throwError('error'));
    service.load(node);
    expect(service.state).toBe('error');
  });

  it('should save', () => {
    const fileNode = testStorageFileNode();
    const directoryNode = testStorageDirectoryNode();
    service._contentSubject.next('content');
    service._node = fileNode;
    service.save();
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/set/content');
    expect(req.request.method).toBe('POST');
    expect(req.request.params.get('path')).toEqual(fileNode.path);
    expect(req.request.body).toEqual('content');
    req.flush(directoryNode);
    httpTestingController.verify();
    expect(service._node).toBe(directoryNode);
    expect(publish).toHaveBeenCalledWith(new NodePendingSaveEvent(directoryNode, false));
  });

  it('should handle _nodeModified', () => {
    const fileNode = testStorageFileNode();
    service._node = fileNode;
    service._nodeModified(new NodeModifiedEvent(fileNode));
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/get/content');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('path')).toEqual(fileNode.path);
    req.flush('content');
    httpTestingController.verify();
    expect(service.content).toBe('content');
  });

  it('should filter NodeModifiedEvent (other file)', fakeAsync(() => {
    service._node = testStorageDirectoryNode();
    eventBus.publish(new NodeModifiedEvent(testStorageFileNode()));
    tick(StorageNodeEditorContentService.DEBOUNCE_DELAY);
    httpTestingController.verify();
  }));

  it('should filter NodeModifiedEvent (same lastModified)', fakeAsync(() => {
    const node = testStorageFileNode();
    service._node = node;
    eventBus.publish(new NodeModifiedEvent(node));
    tick(StorageNodeEditorContentService.DEBOUNCE_DELAY);
    httpTestingController.verify();
  }));

  it('should not filter NodeModifiedEvent', fakeAsync(() => {
    service._node = testStorageFileNode();
    eventBus.publish(new NodeModifiedEvent(_.assign(testStorageFileNode(), {lastModified: 99999})));
    tick(StorageNodeEditorContentService.DEBOUNCE_DELAY);
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/get/content');
    expect(req.request.method).toBe('GET');
    req.flush('content');
    httpTestingController.verify();
  }));

  it('should filter SaveNodeEvent (no node)', () => {
    eventBus.publish(new SaveNodeEvent(testStorageFileNode()));
    httpTestingController.verify();
  });

  it('should filter SaveNodeEvent (other node)', () => {
    service._node = testStorageDirectoryNode();
    eventBus.publish(new SaveNodeEvent(testStorageFileNode()));
    httpTestingController.verify();
  });

  it('should not filter SaveNodeEvent', () => {
    const node = testStorageFileNode();
    service._node = node;
    eventBus.publish(new SaveNodeEvent(node));
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/set/content');
    expect(req.request.method).toBe('POST');
    req.flush(node);
    httpTestingController.verify();
  });

  it('should filter content next (no node)', fakeAsync(() => {
    service._contentSubject.next('content');
    tick(StorageNodeEditorContentService.DEBOUNCE_DELAY);
    httpTestingController.verify();
  }));

  it('should filter content next (null content)', fakeAsync(() => {
    service._node = testStorageFileNode();
    service._contentSubject.next(null);
    tick(StorageNodeEditorContentService.DEBOUNCE_DELAY);
    httpTestingController.verify();
  }));

  it('should not filter content next', fakeAsync(() => {
    const node = testStorageFileNode();
    service._node = node;
    service._contentSubject.next('content');
    tick(StorageNodeEditorContentService.DEBOUNCE_DELAY);
    expect(eventBus.publish).toHaveBeenCalledWith(new NodePendingSaveEvent(node, true));
    const req = httpTestingController.expectOne(request => request.url === 'storageApiUrl/files/set/content');
    expect(req.request.method).toBe('POST');
    req.flush(node);
    httpTestingController.verify();
  }));

});
