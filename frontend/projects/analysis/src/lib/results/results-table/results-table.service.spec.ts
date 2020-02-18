import {TestBed} from '@angular/core/testing';

import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {Result, ResultStatus} from 'projects/analysis/src/lib/entities/result';
import {of, ReplaySubject} from 'rxjs';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {EventEmitter} from '@angular/core';
import {IsDebugEntryStorageNodePipe} from 'projects/analysis/src/lib/results/is-debug-entry-storage-node.pipe';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {OpenDebugEvent} from 'projects/analysis/src/lib/events/open-debug-event';
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {storageListServiceSpy} from 'projects/storage/src/lib/storage-list.service.spec';
import SpyObj = jasmine.SpyObj;
import {PathToNamePipe} from 'projects/tools/src/lib/path-to-name.pipe';
import {PathToParentPathPipe} from 'projects/tools/src/lib/path-to-parent-path.pipe';

export const testResultStatus: (status: ResultStatus) => Result = (status: ResultStatus) => {
  return {
    id: 'uuid',
    startDate: 42,
    endDate: 1337,
    status: status,
    description: 'BasicSimulation',
    type: 'RUN',
  };
};

export const testResult: () => Result = () => {
  return testResultStatus('COMPLETED');
};

export const testResultDebug: () => Result = () => {
  return {
    id: 'debug-uuid',
    startDate: 42,
    endDate: 1337,
    status: 'COMPLETED',
    description: 'BasicSimulation',
    type: 'DEBUG',
  };
};

export const testResultNode: () => StorageNode = () => {
  return {
    path: 'gatling/results/uuid/result.json',
    type: 'FILE',
    depth: 3,
    length: 0,
    lastModified: 0
  };
};

export const resultsTableServiceSpy: () => SpyObj<ResultsTableService> = () => {
  const spy = jasmine.createSpyObj('ResultsTableService', [
    'ngOnDestroy',
    'init',
    'select',
    'isSelected',
    'get',
  ]);
  spy.valuesSubject = new ReplaySubject<Result[]>(1);
  spy.selectionChanged = new EventEmitter();
  spy.values = [];
  return spy;
};

describe('ResultsTableService', () => {
  let service: ResultsTableService;
  let storage: SpyObj<StorageService>;
  let storageList: SpyObj<StorageListService>;
  let events: SpyObj<EventBusService>;
  let localStorage: LocalStorageService;
  let resultNode: StorageNode;
  let result: Result;
  let debugResult: Result;

  beforeEach(() => {
    resultNode = testResultNode();
    result = testResult();
    debugResult = testResultDebug();
    storage = storageServiceSpy();
    storage.listJSON.and.returnValue(of([]));
    storageList = storageListServiceSpy();

    TestBed.configureTestingModule({
      imports: [
        CoreTestModule,
      ],
      providers: [
        {provide: StorageService, useValue: storage},
        {provide: StorageListService, useValue: storageList},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        StorageNodeToNamePipe,
        PathToNamePipe,
        StorageNodeToParentPathPipe,
        PathToParentPathPipe,
        NodeEventToNodePipe,
        ResultsTableService,
        IsDebugEntryStorageNodePipe,
      ]
    });
    service = TestBed.get(ResultsTableService);
    events = TestBed.get(EventBusService);
    localStorage = TestBed.get(LocalStorageService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should find', () => {
    service.values = [result];
    expect(service.find(resultNode)).toBe(result);
  });

  it('should return hasSelection', () => {
    expect(service.hasSelection).toBeFalsy();
    service.selection = result;
    expect(service.hasSelection).toBeTruthy();
  });

  it('should return selection', () => {
    expect(service.selection).toBeNull();
    service.selection = result;
    expect(service.selection).toEqual(result);
  });

  it('should return isSelected', () => {
    expect(service.isSelected(result)).toBeFalsy();
    service.selection = result;
    expect(service.isSelected(result)).toBeTruthy();
  });

  it('should setSelection clear', () => {
    service.selection = result;
    service.selection = null;
    expect(service.hasSelection).toBeFalse();
  });

  it('should select emit', () => {
    const spy = spyOn(service._selection, 'select');
    service.selection = result;
    expect(spy).toHaveBeenCalledWith(result);
  });

  it('should init selection', () => {
    spyOn(localStorage, 'getItem').and.returnValue(result);
    service.init();
  });

  describe('should init and', () => {
    beforeEach(() => {
      service.init();
    });

    it('should _nodeCreated', () => {
      storage.getJSON.and.returnValue(of(result));
      storageList.nodeCreated.emit(testStorageFileNode());
      expect(service.values[0]).toEqual(result);
    });

    it('should _nodeCreated debug', () => {
      storage.getJSON.and.returnValue(of(debugResult));
      storageList.nodeCreated.emit(testStorageFileNode());
      expect(service.values[0]).toEqual(debugResult);
      expect(service.selection).toEqual(debugResult);
    });

    it('should _nodeCreated prevent duplicates', () => {
      service.values = [result];
      storage.getJSON.and.returnValue(of(result));
      storageList.nodeCreated.emit(testStorageFileNode());
      expect(service.values.length).toBe(1);
    });

    it('should handle selection change', () => {
      spyOn(events, 'publish');
      service.selection = result;
      expect(localStorage.getItem(ResultsTableService.ID)).toEqual(result);
      expect(events.publish).not.toHaveBeenCalledWith(new OpenDebugEvent());
    });

    it('should handle selection change debug', () => {
      spyOn(events, 'publish');
      service.selection = debugResult;
      expect(localStorage.getItem(ResultsTableService.ID)).toEqual(debugResult);
      expect(events.publish).toHaveBeenCalledWith(new OpenDebugEvent());
    });

    it('should handle selection clear', () => {
      service.selection = debugResult;
      (service as any)._selection.clear();
      expect(localStorage.getItem(ResultsTableService.ID)).toBeUndefined();
    });

    it('should update selection on list change', () => {
      service.selection = debugResult;
      service.valuesSubject.next([result, debugResult]);
      expect(service.isSelected(debugResult)).toBeTruthy();
    });

    it('should update selection on list change', () => {
      service.selection = debugResult;
      service.valuesSubject.next([result]);
      expect(service.hasSelection).toBeFalsy();
    });

    it('should not update selection on list change', () => {
      service.valuesSubject.next([result]);
      expect(service.hasSelection).toBeFalsy();
    });

    it('should update selection on editor change', () => {
      service.values = [debugResult];
      events.publish(new SelectNodeEvent({
        path: 'gatling/results/debug-uuid/debug/my_request.debug',
        type: 'FILE',
        depth: 4,
        length: 42,
        lastModified: 1337
      }));
      expect(service.isSelected(debugResult)).toBeTruthy();
    });

  });

});

