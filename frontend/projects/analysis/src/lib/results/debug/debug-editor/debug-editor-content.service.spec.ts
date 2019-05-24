import {TestBed} from '@angular/core/testing';

import {DebugEditorContentService} from 'projects/analysis/src/lib/results/debug/debug-editor/debug-editor-content.service';
import {EventEmitter} from '@angular/core';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {storageConfigurationServiceSpy} from 'projects/storage/src/lib/storage-configuration.service.spec';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {windowSpy} from 'projects/tools/src/lib/window.service.spec';
import {testDebugChunk} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.service.spec';
import {of, throwError} from 'rxjs';
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {DebugChunkToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-path.pipe';
import {ResultsListService} from 'projects/analysis/src/lib/results/results-list.service';
import {resultsListServiceSpy} from 'projects/analysis/src/lib/results/results-list.service.spec';
import SpyObj = jasmine.SpyObj;

export const debugEditorContentServiceSpy = () => {
  const spy = jasmine.createSpyObj('DebugEditorContentService', [
    'load',
    'ngOnDestroy',
  ]);
  spy.contentChanged = new EventEmitter<string>();
  spy.content = '';
  return spy;
};

describe('DebugEditorContentService', () => {

  let service: DebugEditorContentService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;
  let storage: SpyObj<StorageService>;
  let window: SpyObj<WindowService>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: StorageConfigurationService, useValue: storageConfigurationServiceSpy()},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        {provide: WindowService, useValue: windowSpy()},
        {provide: StorageService, useValue: storageServiceSpy()},
        {provide: ResultsListService, useValue: resultsListServiceSpy()},
        DebugEditorContentService,
        DebugChunkToPathPipe,
      ]
    });
    service = TestBed.get(DebugEditorContentService);
    storage = TestBed.get(StorageService);
    window = TestBed.get(WindowService);
    httpTestingController = TestBed.get(HttpTestingController);
    eventBus = TestBed.get(EventBusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should openResponseBody', () => {
    const chunk = testDebugChunk();
    storage.getJSON.and.returnValue(of(chunk));
    storage.getContent.and.returnValue(of('content'));
    service.load(testStorageFileNode());

    window.open.and.callFake(url => url.subscribe((value) => expect(value).toEqual('staticApiUrl/gatling/results/debug-uuid/debug/responseBodyFile')));
    service.openResponseBody();
    expect(window.open).toHaveBeenCalled();
  });

  it('should not load chunk', () => {
    storage.getJSON.and.returnValue(throwError('fail'));
    service.load(testStorageFileNode());
    expect(service.state).toBe('error');
  });

  it('should not load content', () => {
    const chunk = testDebugChunk();
    storage.getJSON.and.returnValue(of(chunk));
    storage.getContent.and.returnValue(throwError('fail'));
    service.load(testStorageFileNode());
    expect(service.state).toBe('error');
  });

  it('should load', () => {
    const chunk = testDebugChunk();
    storage.getJSON.and.returnValue(of(chunk));
    storage.getContent.and.returnValue(of('content'));
    service.load(testStorageFileNode());

    expect(service.chunk).toBe(chunk);
    expect(service.requestHeadersFlex).toBe('calc(35% - 72px)');
    expect(service.requestCookiesFlex).toBe('25%');
    expect(service.requestBodyFlex).toBe('calc(40% - 36px)');
    expect(service.responseHeadersFlex).toBe('calc(40% - 36px)');
    expect(service.responseBodyFlex).toBe('calc(60% - 36px)');

    expect(service.hasRequestCookies).toBeTruthy();
    expect(service.hasRequestBody).toBeTruthy();
    expect(service.hasResponseBody).toBeTruthy();

    expect(service.requestBody).toBe('content');
    expect(service.responseBody).toBe('content');

    expect(storage.getJSON).toHaveBeenCalledWith(testStorageFileNode());
    expect(storage.getContent).toHaveBeenCalledWith({path: 'gatling/results/debug-uuid/debug/requestBodyFile'});
    expect(storage.getContent).toHaveBeenCalledWith({path: 'gatling/results/debug-uuid/debug/responseBodyFile'});
  });

  it('should load minimal', () => {
    const chunk: DebugChunk = {
      id: 'some-id',
      resultId: 'resultId',
      date: 42,
      requestName: 'requestName',
      requestStatus: 'requestStatus',
      session: 'session',
      requestUrl: 'requestUrl',
      requestHeaders: [{key: 'key', value: 'value'}],
      requestCookies: [],
      requestBodyFile: '',
      responseStatus: 'responseStatus',
      responseHeaders: [{key: 'key', value: 'value'}],
      responseBodyFile: '',
    };
    storage.getJSON.and.returnValue(of(chunk));
    service.load(testStorageFileNode());
    expect(service.chunk).toBe(chunk);
    expect(service.requestHeadersFlex).toBe('calc(100% - 72px)');
    expect(service.requestCookiesFlex).toBeUndefined();
    expect(service.requestBodyFlex).toBeUndefined();
    expect(service.responseHeadersFlex).toBe('calc(100% - 36px)');
    expect(service.responseBodyFlex).toBeUndefined();

    expect(service.hasRequestCookies).toBeFalsy();
    expect(service.hasRequestBody).toBeFalsy();
    expect(service.hasResponseBody).toBeFalsy();

    expect(service.requestBody).toBeUndefined();
    expect(service.responseBody).toBeUndefined();
  });

  it('should load no bodies', () => {
    const chunk: DebugChunk = {
      id: 'some-id',
      resultId: 'resultId',
      date: 42,
      requestName: 'requestName',
      requestStatus: 'requestStatus',
      session: 'session',
      requestUrl: 'requestUrl',
      requestHeaders: [{key: 'key', value: 'value'}],
      requestCookies: ['cookie'],
      requestBodyFile: '',
      responseStatus: 'responseStatus',
      responseHeaders: [{key: 'key', value: 'value'}],
      responseBodyFile: '',
    };
    storage.getJSON.and.returnValue(of(chunk));
    service.load(testStorageFileNode());
    expect(service.chunk).toBe(chunk);
    expect(service.requestHeadersFlex).toBe('calc(65% - 72px)');
    expect(service.requestCookiesFlex).toBe('35%');
    expect(service.requestBodyFlex).toBeUndefined();
    expect(service.responseHeadersFlex).toBe('calc(100% - 36px)');
    expect(service.responseBodyFlex).toBeUndefined();

    expect(service.hasRequestCookies).toBeTruthy();
    expect(service.hasRequestBody).toBeFalsy();
    expect(service.hasResponseBody).toBeFalsy();

    expect(service.requestBody).toBeUndefined();
    expect(service.responseBody).toBeUndefined();
  });

  it('should load no cookies', () => {
    const chunk: DebugChunk = {
      id: 'some-id',
      resultId: 'resultId',
      date: 42,
      requestName: 'requestName',
      requestStatus: 'requestStatus',
      session: 'session',
      requestUrl: 'requestUrl',
      requestHeaders: [{key: 'key', value: 'value'}],
      requestCookies: [],
      requestBodyFile: 'requestBodyFile',
      responseStatus: 'responseStatus',
      responseHeaders: [{key: 'key', value: 'value'}],
      responseBodyFile: 'responseBodyFile',
    };
    storage.getJSON.and.returnValue(of(chunk));
    storage.getContent.and.returnValue(of('content'));
    service.load(testStorageFileNode());
    expect(service.chunk).toBe(chunk);
    expect(service.requestHeadersFlex).toBe('calc(55% - 72px)');
    expect(service.requestCookiesFlex).toBeUndefined();
    expect(service.requestBodyFlex).toBe('calc(45% - 36px)');
    expect(service.responseHeadersFlex).toBe('calc(40% - 36px)');
    expect(service.responseBodyFlex).toBe('calc(60% - 36px)');

    expect(service.hasRequestCookies).toBeFalsy();
    expect(service.hasRequestBody).toBeTruthy();
    expect(service.hasResponseBody).toBeTruthy();

    expect(service.requestBody).toBe('content');
    expect(service.responseBody).toBe('content');
  });
});
