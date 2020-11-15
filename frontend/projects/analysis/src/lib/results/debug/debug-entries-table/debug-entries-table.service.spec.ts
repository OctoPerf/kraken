import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {of, ReplaySubject} from 'rxjs';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {DebugEntry} from 'projects/analysis/src/lib/entities/debug-entry';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {storageListServiceSpy} from 'projects/storage/src/lib/storage-list.service.spec';
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';
import {DebugEntriesTableService} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.service';
import {
  resultsTableServiceSpy,
  testResult
} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';
import {DebugEntryToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-path.pipe';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {EditorDialogService} from 'projects/dialog/src/lib/editor-dialogs/editor-dialog.service';
import {editorDialogServiceSpy} from 'projects/dialog/src/lib/editor-dialogs/editor-dialog.service.spec';
import SpyObj = jasmine.SpyObj;
import {InjectDialogService} from 'projects/dialog/src/lib/inject-dialogs/inject-dialog.service';
import {injectDialogServiceSpy} from 'projects/dialog/src/lib/inject-dialogs/inject-dialog.service.spec';

export const testDebugEntry: () => DebugEntry = () => {
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

export const debugEntriesTableServiceSpy: () => SpyObj<DebugEntriesTableService> = () => {
  const spy = jasmine.createSpyObj('DebugEntriesTableService', [
    'ngOnDestroy',
    'init',
    'open',
    'compare',
    'isSelected',
  ]);
  spy.valuesSubject = new ReplaySubject<DebugEntry[]>(1);
  return spy;
};

describe('DebugEntriesTableService', () => {
  let service: DebugEntriesTableService;
  let storage: SpyObj<StorageService>;
  let storageList: SpyObj<StorageListService>;
  let eventBus: SpyObj<EventBusService>;
  let dialogs: SpyObj<InjectDialogService>;
  let results: SpyObj<ResultsTableService>;

  let debugEntryNode: StorageNode;
  let debugEntry: DebugEntry;

  beforeEach(() => {
    debugEntryNode = testDebugResultNode();
    debugEntry = testDebugEntry();
    storage = storageServiceSpy();
    storage.listJSON.and.returnValue(of([]));
    storageList = storageListServiceSpy();

    TestBed.configureTestingModule({
      providers: [
        {provide: StorageService, useValue: storage},
        {provide: StorageListService, useValue: storageList},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        {provide: ResultsTableService, useValue: resultsTableServiceSpy()},
        {provide: InjectDialogService, useValue: injectDialogServiceSpy()},
        DebugEntryToPathPipe,
        StorageNodeToNamePipe,
        NodeEventToNodePipe,
        DebugEntriesTableService,
      ]
    });
    service = TestBed.inject(DebugEntriesTableService);
    results = TestBed.inject(ResultsTableService) as SpyObj<ResultsTableService>;
    eventBus = TestBed.inject(EventBusService) as SpyObj<EventBusService>;
    dialogs = TestBed.inject(InjectDialogService) as SpyObj<InjectDialogService>;
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should find', () => {
    service.values = [debugEntry];
    expect(service.find(debugEntryNode)).toBe(debugEntry);
  });

  it('should compare', () => {
    dialogs.open.and.returnValue(of('ok'));
    service.compare();
    expect(dialogs.open).toHaveBeenCalled();
  });

  it('should open', () => {
    storage.get.and.returnValue(of(debugEntryNode));
    service.open(debugEntry);
    expect(storage.get).toHaveBeenCalledWith(debugEntryNode.path);
    expect(storage.edit).toHaveBeenCalledWith(debugEntryNode);
  });

  it('should not init', () => {
    service.init();
    expect(storageList.init).not.toHaveBeenCalled();
  });

  describe('should init and', () => {
    beforeEach(() => {
      results._selection.selection = testResult();
      service.init();
    });

    it('should list', () => {
      expect(storageList.init).toHaveBeenCalledWith('gatling/results/uuid', '.*\\.debug', 2);
    });

    it('should unselect node', () => {
      service._selection.selection = debugEntry;
      eventBus.publish(new SelectNodeEvent(null));
      expect(service._selection.selection).toBeNull();
    });

    it('should select do not find node', () => {
      eventBus.publish(new SelectNodeEvent(debugEntryNode));
      expect(service._selection.selection).toBeNull();
    });

    it('should select', () => {
      spyOn(service, 'find').and.returnValue(debugEntry);
      eventBus.publish(new SelectNodeEvent(debugEntryNode));
      expect(service._selection.selection).toEqual(debugEntry);
    });

    it('should _nodesListed', fakeAsync(() => {
      const nodes = [testStorageFileNode()];
      const values = [testDebugEntry()];
      storage.listJSON.and.returnValue(of(values));
      storageList.nodesListed.emit(nodes);
      tick(500);
      expect(service.values).toBe(values);
      expect(service.loading).toBeFalse();
    }));
  });
});
