import {TestBed} from '@angular/core/testing';

import {GatlingResultService} from 'projects/analysis/src/lib/results/results-table/gatling-result.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {of} from 'rxjs';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {windowSpy} from 'projects/tools/src/lib/window.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {
  testResult,
  testResultDebug,
  testResultNode,
  testResultStatus
} from 'projects/analysis/src/lib/results/results-table/results-table.service.spec';
import {AnalysisService} from 'projects/analysis/src/lib/analysis.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisServiceSpy} from 'projects/analysis/src/lib/analysis.service.spec';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import SpyObj = jasmine.SpyObj;


export const gatlingResultServiceSpy = () => {
  const spy = jasmine.createSpyObj('GatlingResultService', [
    'deleteResult',
    'openGatlingReport',
    'listGatlingReport',
    'canOpenGrafanaReport',
    'openGrafanaReport',
    'canDeleteResult',
  ]);
  return spy;
};

describe('GatlingResultService', () => {
  let service: GatlingResultService;
  let storage: SpyObj<StorageService>;
  let events: SpyObj<EventBusService>;
  let window: SpyObj<WindowService>;
  let analysis: SpyObj<AnalysisService>;
  let dialogs: SpyObj<DialogService>;

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
        {provide: DialogService, useValue: dialogsServiceSpy()},
        GatlingResultService,
      ]
    });
    service = TestBed.inject(GatlingResultService);
    analysis = TestBed.inject(AnalysisService) as SpyObj<AnalysisService>;
    storage = TestBed.inject(StorageService) as SpyObj<StorageService>;
    events = TestBed.inject(EventBusService) as SpyObj<EventBusService>;
    window = TestBed.inject(WindowService) as SpyObj<WindowService>;
    dialogs = TestBed.inject(DialogService) as SpyObj<DialogService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should delete result', () => {
    dialogs.delete.and.returnValue(of(null));
    analysis.deleteTest.and.returnValue(of('testId'));
    service.deleteResult(result).subscribe();
    expect(dialogs.delete).toHaveBeenCalledWith('test result', [result.description], false);
    expect(analysis.deleteTest).toHaveBeenCalledWith(result.id);
  });

  it('should openGrafanaReport', () => {
    window.open.and.callFake(url => url.subscribe(value => expect(value).toBe('grafanaUrl/uuid')));
    service.openGrafanaReport(result);
    expect(window.open).toHaveBeenCalled();
  });

  it('should canOpenGrafanaReport', () => {
    expect(service.canOpenGrafanaReport(testResultStatus('RUNNING'))).toBeTruthy();
    expect(service.canOpenGrafanaReport(testResultDebug())).toBeFalsy();
  });

  it('should canDeleteResult', () => {
    expect(service.canDeleteResult(testResultStatus('RUNNING'))).toBeFalsy();
    expect(service.canDeleteResult(testResultStatus('COMPLETED'))).toBeTruthy();
    expect(service.canDeleteResult(testResultStatus('CANCELED'))).toBeTruthy();
    expect(service.canDeleteResult(testResultStatus('FAILED'))).toBeTruthy();
  });

  it('should canOpenGatlingReport FAILED', () => {
    expect(service.canOpenGatlingReport(testResultStatus('FAILED'))).toBeFalsy();
  });

  it('should canOpenGatlingReport COMPLETED', () => {
    expect(service.canOpenGatlingReport(testResultStatus('COMPLETED'))).toBeTruthy();
  });

  it('should canOpenGatlingReport CANCELED', () => {
    expect(service.canOpenGatlingReport(testResultStatus('CANCELED'))).toBeTruthy();
  });

  it('should canOpenGatlingReport HAR', () => {
    const harResult = testResultDebug();
    harResult.type = 'HAR';
    expect(service.canOpenGatlingReport(harResult)).toBeFalsy();
  });

});


