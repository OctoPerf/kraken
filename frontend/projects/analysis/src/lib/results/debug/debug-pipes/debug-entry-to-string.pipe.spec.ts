import {DebugEntry} from 'projects/analysis/src/lib/entities/debug-entry';
import {of, ReplaySubject} from 'rxjs';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {DateTimeToStringMsPipe} from 'projects/date/src/lib/date-time-to-string-ms.pipe';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {TestBed} from '@angular/core/testing';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import SpyObj = jasmine.SpyObj;
import {DebugEntryToStringPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-string.pipe';
import {DebugEntryToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-path.pipe';
import {testDebugEntry} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.service.spec';

export const debugEntryToStringPipeSpy: () => SpyObj<DebugEntryToStringPipe> = () => {
  const spy = jasmine.createSpyObj('DebugEntryToStringPipe', [
    'transform',
  ]);
  spy.entriesSubject = new ReplaySubject<Result[]>(1);
  spy.valuesSubject = new ReplaySubject<DebugEntry[]>(1);
  return spy;
};

describe('DebugEntryToStringPipe', () => {

  let pipe: DebugEntryToStringPipe;
  let storage: SpyObj<StorageService>;

  beforeEach(() => {
    storage = storageServiceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: StorageService, useValue: storage},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        DateTimeToStringMsPipe,
        DebugEntryToPathPipe,
        DebugEntryToStringPipe,
      ]
    });
    pipe = TestBed.inject(DebugEntryToStringPipe);
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('transform', () => {
    storage.getContent.and.returnValue(of('body'));
    let str = null;
    pipe.transform(testDebugEntry()).subscribe(value => str = value);
    expect(str).toBeDefined();
  });

  it('transform no bodies', () => {
    const entry = testDebugEntry();
    entry.responseBodyFile = null;
    entry.requestBodyFile = null;
    let str = null;
    pipe.transform(entry).subscribe(value => str = value);
    expect(str).toBeDefined();
    expect(storage.getContent).not.toHaveBeenCalled();
  });
});

