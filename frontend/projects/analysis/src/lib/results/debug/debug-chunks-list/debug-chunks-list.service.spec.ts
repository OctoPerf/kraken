import {TestBed} from '@angular/core/testing';

import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {of, ReplaySubject} from 'rxjs';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import {DebugChunksListService} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {DebugChunkToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-path.pipe';
import {ResultsListService} from 'projects/analysis/src/lib/results/results-list.service';
import {resultsListServiceSpy, testResult} from 'projects/analysis/src/lib/results/results-list.service.spec';
import * as _ from 'lodash';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {storageListServiceSpy} from 'projects/storage/src/lib/storage-list.service.spec';
import SpyObj = jasmine.SpyObj;
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';

export const testDebugChunk: () => DebugChunk = () => {
  return {
    id: 'some-id',
    resultId: 'debug-uuid',
    date: 42,
    requestName: 'requestName',
    requestStatus: 'requestStatus',
    session: 'session',
    requestUrl: 'requestUrl',
    requestHeaders: [{key: 'key', value: 'value'}],
    requestCookies: ['cookie'],
    requestBodyFile: 'requestBodyFile',
    responseStatus: 'responseStatus',
    responseHeaders: [{key: 'key', value: 'value'}],
    responseBodyFile: 'responseBodyFile',
  };
};

export const testDebugResultNode: () => StorageNode = () => {
  return {
    path: 'gatling/results/debug-uuid/debug/some-id.debug',
    type: 'FILE',
    depth: 2,
    length: 0,
    lastModified: 0
  };
};

export const debugResultListServiceSpy: () => SpyObj<DebugChunksListService> = () => {
  const spy = jasmine.createSpyObj('DebugChunksListService', [
    'ngOnDestroy',
    'init',
    'open',
    'compare',
    'isSelected',
  ]);
  spy.valuesSubject = new ReplaySubject<DebugChunk[]>(1);
  return spy;
};

describe('DebugChunksListService', () => {
  let service: DebugChunksListService;
  let storage: SpyObj<StorageService>;
  let storageList: SpyObj<StorageListService>;
  let eventBus: SpyObj<EventBusService>;
  let dialogs: SpyObj<DialogService>;
  let results: SpyObj<ResultsListService>;

  let debugChunkNode: StorageNode;
  let debugChunk: DebugChunk;

  beforeEach(() => {
    debugChunkNode = testDebugResultNode();
    debugChunk = testDebugChunk();
    storage = storageServiceSpy();
    storage.listJSON.and.returnValue(of([]));
    storageList = storageListServiceSpy();

    TestBed.configureTestingModule({
      providers: [
        {provide: StorageService, useValue: storage},
        {provide: StorageListService, useValue: storageList},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        {provide: ResultsListService, useValue: resultsListServiceSpy()},
        {provide: DialogService, useValue: dialogsServiceSpy()},
        DebugChunkToPathPipe,
        StorageNodeToNamePipe,
        NodeEventToNodePipe,
        DebugChunksListService,
      ]
    });
    service = TestBed.get(DebugChunksListService);
    results = TestBed.get(ResultsListService);
    eventBus = TestBed.get(EventBusService);
    dialogs = TestBed.get(DialogService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should find', () => {
    service.values = [debugChunk];
    expect(service.find(debugChunkNode)).toBe(debugChunk);
  });

  it('should compare', () => {
    dialogs.open.and.returnValue(of('ok'));
    service.compare();
    expect(dialogs.open).toHaveBeenCalled();
  });

  it('should open', () => {
    storage.get.and.returnValue(of(debugChunkNode));
    service.open(debugChunk);
    expect(storage.get).toHaveBeenCalledWith(debugChunkNode.path);
    expect(storage.edit).toHaveBeenCalledWith(debugChunkNode);
  });

  it('should isSelected', () => {
    expect(service.isSelected(debugChunk)).toBeFalsy();
    service.selection = debugChunk;
    expect(service.isSelected(debugChunk)).toBeTruthy();
    expect(service.isSelected(_.cloneDeep(debugChunk))).toBeFalsy();
  });

  it('should not init', () => {
    service.init();
    expect(storageList.init).not.toHaveBeenCalled();
  });

  describe('should init and', () => {
    beforeEach(() => {
      results.selection = testResult();
      service.init();
    });

    it('should list', () => {
      expect(storageList.init).toHaveBeenCalledWith('gatling/results/uuid', '.*\\.debug', 2);
    });

    it('should unselect node', () => {
      service.selection = debugChunk;
      eventBus.publish(new SelectNodeEvent(null));
      expect(service.selection).toBeNull();
    });

    it('should select do not find node', () => {
      eventBus.publish(new SelectNodeEvent(debugChunkNode));
      expect(service.selection).toBeNull();
    });

    it('should select', () => {
      spyOn(service, 'find').and.returnValue(debugChunk);
      eventBus.publish(new SelectNodeEvent(debugChunkNode));
      expect(service.selection).toEqual(debugChunk);
    });
  });
});
