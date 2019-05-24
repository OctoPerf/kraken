import {DebugChunkToStringPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-string.pipe';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import {of, ReplaySubject} from 'rxjs';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {DateTimeToStringMsPipe} from 'projects/date/src/lib/date-time-to-string-ms.pipe';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {DebugChunkToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-path.pipe';
import {TestBed} from '@angular/core/testing';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {testDebugChunk} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import SpyObj = jasmine.SpyObj;

export const debugChunkToStringPipeSpy: () => SpyObj<DebugChunkToStringPipe> = () => {
  const spy = jasmine.createSpyObj('DebugChunkToStringPipe', [
    'transform',
  ]);
  spy.chunksSubject = new ReplaySubject<Result[]>(1);
  spy.valuesSubject = new ReplaySubject<DebugChunk[]>(1);
  return spy;
};

describe('DebugChunkToStringPipe', () => {

  let pipe: DebugChunkToStringPipe;
  let storage: SpyObj<StorageService>;

  beforeEach(() => {
    storage = storageServiceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: StorageService, useValue: storage},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        DateTimeToStringMsPipe,
        DebugChunkToPathPipe,
        DebugChunkToStringPipe,
      ]
    });
    pipe = TestBed.get(DebugChunkToStringPipe);
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('transform', () => {
    storage.getContent.and.returnValue(of('body'));
    let str = null;
    pipe.transform(testDebugChunk()).subscribe(value => str = value);
    expect(str).toBeDefined();
  });

  it('transform no bodies', () => {
    const chunk = testDebugChunk();
    chunk.responseBodyFile = null;
    chunk.requestBodyFile = null;
    let str = null;
    pipe.transform(chunk).subscribe(value => str = value);
    expect(str).toBeDefined();
    expect(storage.getContent).not.toHaveBeenCalled();
  });
});

