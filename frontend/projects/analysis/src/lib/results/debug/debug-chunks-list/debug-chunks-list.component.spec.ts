import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {
  debugResultListServiceSpy,
  testDebugChunk
} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.service.spec';
import {DebugChunksListComponent} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.component';
import {DebugChunksListService} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {storageServiceSpy} from 'projects/storage/src/lib/storage.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';
import {ResultsListService} from 'projects/analysis/src/lib/results/results-list.service';
import {resultsListServiceSpy} from 'projects/analysis/src/lib/results/results-list.service.spec';
import SpyObj = jasmine.SpyObj;
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {storageListServiceSpy} from 'projects/storage/src/lib/storage-list.service.spec';

describe('DebugChunksListComponent', () => {
  let component: DebugChunksListComponent;
  let fixture: ComponentFixture<DebugChunksListComponent>;
  let debugResult: SpyObj<DebugChunksListService>;
  let storage: SpyObj<StorageService>;

  beforeEach(async(() => {
    debugResult = debugResultListServiceSpy();
    storage = storageServiceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [DebugChunksListComponent],
      providers: [
        {provide: DebugChunksListService, useValue: debugResult},
        {provide: StorageService, useValue: storage},
        {provide: AnalysisConfigurationService, useValue: analysisConfigurationServiceSpy()},
        {provide: ResultsListService, useValue: resultsListServiceSpy()},
        NodeEventToNodePipe,
      ]
    })
      .overrideProvider(StorageListService, {useValue: storageListServiceSpy()})
      .overrideProvider(DebugChunksListService, {useValue: debugResult})
      .overrideTemplate(DebugChunksListComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DebugChunksListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init datasource', () => {
    debugResult.valuesSubject.next([testDebugChunk()]);
    expect(component.dataSource).toBeTruthy();
  });

});
