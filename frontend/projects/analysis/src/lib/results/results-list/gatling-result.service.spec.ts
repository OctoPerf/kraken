import {TestBed} from '@angular/core/testing';

import {GatlingResultService} from 'projects/analysis/src/lib/results/results-list/gatling-result.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {of, throwError} from 'rxjs';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {windowSpy} from 'projects/tools/src/lib/window.service.spec';
import {GatlingConfigurationService} from 'projects/gatling/src/app/gatling-configuration.service';
import {gatlingConfigurationServiceSpy} from 'projects/gatling/src/app/gatling-configuration.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {
  testResult, testResultDebug,
  testResultNode,
  testResultStatus
} from 'projects/analysis/src/lib/results/results-list.service.spec';
import {AnalysisService} from 'projects/analysis/src/lib/analysis.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisServiceSpy} from 'projects/analysis/src/lib/analysis.service.spec';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import SpyObj = jasmine.SpyObj;


export const gatlingResultServiceSpy = () => {
  const spy = jasmine.createSpyObj('GatlingResultService', [
    'deleteResult',
    'openGatlingReport',
  ]);
  return spy;
};

describe('GatlingResultService', () => {
  let service: GatlingResultService;
  let storage: SpyObj<StorageService>;
  let events: SpyObj<EventBusService>;
  let window: SpyObj<WindowService>;
  let analysis: SpyObj<AnalysisService>;

  let resultsRootNode: StorageNode;
  let resultNode: StorageNode;
  let result: Result;

  beforeEach(() => {
    resultsRootNode = {path: 'gatling/results', type: 'DIRECTORY', depth: 1, length: 0, lastModified: 0};
    resultNode = testResultNode();
    result = testResult();

    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: WindowService, useValue: windowSpy()},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        {provide: AnalysisService, useValue: analysisServiceSpy()},
        GatlingResultService,
      ]
    });
    service = TestBed.get(GatlingResultService);
    analysis = TestBed.get(AnalysisService);
    storage = TestBed.get(StorageService);
    events = TestBed.get(EventBusService);
    window = TestBed.get(WindowService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should delete result', () => {
    analysis.deleteTest.and.returnValue(of('testId'));
    service.deleteResult(result).subscribe();
    expect(analysis.deleteTest).toHaveBeenCalledWith(result.id);
  });

  it('should openGatlingReport', () => {
    window.open.and.callFake(url => url.subscribe(value => expect(value).toBe('staticApiUrl/spotbugs/main.html')));
    const node = testStorageFileNode();
    storage.find.and.returnValue(of([node]));
    service.openGatlingReport(result);
    expect(window.open).toHaveBeenCalled();
    expect(storage.find).toHaveBeenCalledWith('gatling/results/uuid', 'index.html');
  });

  it('should not openGatlingReport', () => {
    window.open.and.callFake(url => url.subscribe(val => fail(val), err => expect(err).toEqual('err')));
    storage.find.and.returnValue(throwError('err'));
    service.openGatlingReport(result);
    expect(window.open).toHaveBeenCalled();
    expect(storage.find).toHaveBeenCalledWith('gatling/results/uuid', 'index.html');
    expect(events.publish).toHaveBeenCalled();
  });

  it('should not openGatlingReport (no nodes)', () => {
    window.open.and.callFake(url => url.subscribe(val => fail(val), err => expect(err).toBeDefined()));
    storage.find.and.returnValue(of([]));
    service.openGatlingReport(result);
    expect(window.open).toHaveBeenCalled();
    expect(storage.find).toHaveBeenCalledWith('gatling/results/uuid', 'index.html');
    expect(events.publish).toHaveBeenCalled();
  });

  it('should openGrafanaReport', () => {
    window.open.and.callFake(url => url.subscribe(value => expect(value).toBe('grafanaUrl/uuid')));
    service.openGrafanaReport(result);
    expect(window.open).toHaveBeenCalled();
  });

  it('should canOpenGrafanaReport', () => {
    expect(service.canOpenGrafanaReport(testResultStatus('FAILED'))).toBeFalsy();
    expect(service.canOpenGrafanaReport(testResultStatus('RUNNING'))).toBeTruthy();
    expect(service.canOpenGrafanaReport(testResultDebug())).toBeFalsy();
  });

  it('should canOpenGatlingReport', () => {
    expect(service.canOpenGatlingReport(testResultStatus('FAILED'))).toBeFalsy();
    expect(service.canOpenGatlingReport(testResultStatus('COMPLETED'))).toBeTruthy();
    expect(service.canOpenGatlingReport(testResultStatus('CANCELED'))).toBeTruthy();
  });

  it('should canDeleteResult', () => {
    expect(service.canDeleteResult(testResultStatus('RUNNING'))).toBeFalsy();
    expect(service.canDeleteResult(testResultStatus('COMPLETED'))).toBeTruthy();
    expect(service.canDeleteResult(testResultStatus('CANCELED'))).toBeTruthy();
    expect(service.canDeleteResult(testResultStatus('FAILED'))).toBeTruthy();
  });
});


